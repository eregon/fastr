/*
 * Copyright (c) 2017, 2018, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.r.ffi.impl.nfi;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.InteropException;
import com.oracle.truffle.api.interop.Message;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.r.runtime.RInternalError;
import com.oracle.truffle.r.runtime.ffi.CRFFI;
import com.oracle.truffle.r.runtime.ffi.InvokeCNode;
import com.oracle.truffle.r.runtime.ffi.InvokeCNode.FunctionObjectGetter;
import com.oracle.truffle.r.runtime.ffi.InvokeCNodeGen;

public class TruffleNFI_C implements CRFFI {

    private static final String[] SIGNATURES = new String[32];

    private static String getSignatureForArity(int arity) {
        CompilerAsserts.neverPartOfCompilation();
        if (arity >= SIGNATURES.length || SIGNATURES[arity] == null) {
            StringBuilder str = new StringBuilder().append('(');
            for (int i = 0; i < arity; i++) {
                str.append(i > 0 ? ", " : "");
                str.append("pointer");
            }
            String signature = str.append("): void").toString();
            if (arity < SIGNATURES.length) {
                SIGNATURES[arity] = signature;
            }
            return signature;
        } else {
            return SIGNATURES[arity];
        }
    }

    static final class NFIFunctionObjectGetter extends FunctionObjectGetter {

        @Child private Node bindNode = Message.createInvoke(1).createNode();

        @Override
        @TruffleBoundary
        public TruffleObject execute(TruffleObject address, int arity) {
            // cache signatures
            try {
                return (TruffleObject) ForeignAccess.sendInvoke(bindNode, address, "bind",
                                getSignatureForArity(arity));
            } catch (InteropException ex) {
                throw RInternalError.shouldNotReachHere(ex);
            }
        }
    }

    @Override
    public InvokeCNode createInvokeCNode() {
        return InvokeCNodeGen.create(new NFIFunctionObjectGetter());
    }
}
