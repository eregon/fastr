/*
 * Copyright (c) 2014, 2018, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.truffle.r.ffi.impl.llvm;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.r.ffi.impl.common.LibPaths;
import com.oracle.truffle.r.runtime.context.RContext;
import com.oracle.truffle.r.runtime.ffi.DLLRFFI;

/**
 * When the embedded GNU R is built, LLVM is created for the components of the {@code libRblas} and
 * {@code libRlapack} libraries. In principle we could call the subroutines direct but since,
 * Fortran passes every argument by reference we would have to create many length 1 arrays to wrap
 * the scalar arguments. So we call through a thing veneer in {@code lapack_rffi.c} that forwards
 * the call taking the address of the scalar arguments. We also take the liberty of defining the
 * {@code info} argument taken my most all if the functions in the veneer, and returning the value
 * as the result of the call.
 *
 * N.B. The usual implicit loading of {@code libRlapack} and {@code libRblas} that we get with
 * native {@code dlopen} via {@code libR} does not happen with LLVM, so we must force their loading
 * when this API is requested.
 *
 */
final class TruffleLLVM_Lapack {

    static void load() {
        /*
         * This is a workaround for bad LLVM generated by DragonEgg for (some) of the Lapack
         * functions; additional spurious arguments. Unfortunately for this to be portable we would
         * have to load libR natively to get the rpath support. This code is OS X specific and
         * depends on specific versions.
         */
        RootCallTarget callTarget;
        boolean useLLVM = System.getenv("FASTR_LLVM_LAPACK") != null;
        if (useLLVM) {
            callTarget = openLLVMLibraries();
        } else {
            callTarget = openNativeLibraries();
            // libR must be loaded first to put the rpath mechanism in work. It's OS X specific.
            callTarget.call(LibPaths.getBuiltinLibPath("R_dummy"), false, false);
            callTarget.call(LibPaths.getBuiltinLibPath("gcc_s"), false, true);
            callTarget.call(LibPaths.getBuiltinLibPath("quadmath"), false, true);
            callTarget.call(LibPaths.getBuiltinLibPath("gfortran"), false, true);
        }

        // The following libraries should be loaded eagerly (i.e. with the last parameter true),
        // however, they do not load on Linux due to unbound symbols, such as "xerbla_".
        callTarget.call(LibPaths.getBuiltinLibPath("Rblas"), false, false);
        callTarget.call(LibPaths.getBuiltinLibPath("Rlapack"), false, false);
    }

    private static RootCallTarget openLLVMLibraries() {
        return DLLRFFI.DLOpenRootNode.create(RContext.getInstance());
    }

    private static RootCallTarget openNativeLibraries() {
        TruffleLLVM_NativeDLL.NativeDLOpenRootNode rootNode = TruffleLLVM_NativeDLL.NativeDLOpenRootNode.create();
        return rootNode.getCallTarget();
    }
}
