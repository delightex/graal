/*
 * Copyright (c) 2015, 2017, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.svm.core.posix.headers;

import org.graalvm.nativeimage.Platform;
import org.graalvm.nativeimage.Platforms;
import org.graalvm.nativeimage.c.CContext;
import org.graalvm.nativeimage.c.constant.CConstant;
import org.graalvm.nativeimage.c.constant.CEnum;
import org.graalvm.nativeimage.c.constant.CEnumValue;
import org.graalvm.nativeimage.c.function.CFunction;
import org.graalvm.nativeimage.c.function.CFunctionPointer;
import org.graalvm.nativeimage.c.function.InvokeCFunctionPointer;
import org.graalvm.nativeimage.c.struct.CField;
import org.graalvm.nativeimage.c.struct.CFieldAddress;
import org.graalvm.nativeimage.c.struct.CFieldOffset;
import org.graalvm.nativeimage.c.struct.CPointerTo;
import org.graalvm.nativeimage.c.struct.CStruct;
import org.graalvm.nativeimage.c.type.VoidPointer;
import org.graalvm.nativeimage.c.type.WordPointer;
import org.graalvm.word.PointerBase;

import com.oracle.svm.core.RegisterDumper;
import com.oracle.svm.core.SubstrateSegfaultHandler;
import com.oracle.svm.core.posix.PosixUtils;

// Checkstyle: stop

/**
 * Definitions manually translated from the C header file signal.h.
 */
@CContext(PosixDirectives.class)
public class Signal {

    @CFunction
    public static native int kill(int pid, int sig);

    @CConstant
    public static native int SIG_BLOCK();

    @CConstant
    public static native int SIG_UNBLOCK();

    @CConstant
    public static native int SIG_SETMASK();

    @CFunction
    public static native int sigprocmask(int how, sigset_tPointer set, sigset_tPointer oldset);

    @CPointerTo(nameOfCType = "sigset_t")
    public interface sigset_tPointer extends PointerBase {
    }

    /**
     * Warning: use {@link #sigaction} or {@link PosixUtils#installSignalHandler}. Do NOT introduce
     * calls to {@code signal} or {@code sigset}, which are not portable, and when running in
     * HotSpot, signal chaining (libjsig) will print warnings.
     */
    public interface SignalDispatcher extends CFunctionPointer {
        @InvokeCFunctionPointer
        void dispatch(int sig);
    }

    @CConstant
    public static native SignalDispatcher SIG_DFL();

    @CConstant
    public static native SignalDispatcher SIG_IGN();

    @CConstant
    public static native SignalDispatcher SIG_ERR();

    @CFunction
    public static native int raise(int signum);

    @CStruct(isIncomplete = true)
    public interface siginfo_t extends PointerBase {
        @CField
        int si_signo();

        @CField
        int si_errno();

        @CField
        int si_code();

        @CField
        VoidPointer si_addr();
    }

    @Platforms(Platform.LINUX.class)
    @CPointerTo(nameOfCType = "long long int")
    public interface GregsPointer extends PointerBase {
        long read(int index);
    }

    @Platforms(Platform.DARWIN.class)
    @CPointerTo(nameOfCType = "long long")
    public interface DarwinGregsPointer extends PointerBase {
        long read(int index);
    }

    /**
     * Used in {@link SubstrateSegfaultHandler}. So, this must not be a {@link CEnum} as this would
     * result in machine code that needs a proper a heap base.
     */
    @Platforms({Platform.LINUX_AMD64.class})
    @CContext(PosixDirectives.class)
    public static final class GregEnum {
        @CConstant
        public static native int REG_R8();

        @CConstant
        public static native int REG_R9();

        @CConstant
        public static native int REG_R10();

        @CConstant
        public static native int REG_R11();

        @CConstant
        public static native int REG_R12();

        @CConstant
        public static native int REG_R13();

        @CConstant
        public static native int REG_R14();

        @CConstant
        public static native int REG_R15();

        @CConstant
        public static native int REG_RDI();

        @CConstant
        public static native int REG_RSI();

        @CConstant
        public static native int REG_RBP();

        @CConstant
        public static native int REG_RBX();

        @CConstant
        public static native int REG_RDX();

        @CConstant
        public static native int REG_RAX();

        @CConstant
        public static native int REG_RCX();

        @CConstant
        public static native int REG_RSP();

        @CConstant
        public static native int REG_RIP();

        @CConstant
        public static native int REG_EFL();

        @CConstant
        public static native int REG_CSGSFS();

        @CConstant
        public static native int REG_ERR();

        @CConstant
        public static native int REG_TRAPNO();

        @CConstant
        public static native int REG_OLDMASK();

        @CConstant
        public static native int REG_CR2();
    }

    @CStruct
    public interface ucontext_t extends RegisterDumper.Context {
        @CFieldAddress("uc_mcontext.gregs")
        @Platforms({Platform.LINUX_AMD64.class})
        GregsPointer uc_mcontext_gregs();

        @CFieldAddress("uc_mcontext")
        @Platforms({Platform.LINUX_AARCH64.class, Platform.ANDROID_AARCH64.class})
        mcontext_t uc_mcontext();

        @CField("uc_mcontext")
        @Platforms({Platform.DARWIN_AMD64.class, Platform.IOS_AMD64.class})
        MContext64 uc_mcontext64();

        @CField("uc_mcontext")
        @Platforms({Platform.IOS_AARCH64.class})
        MContext64Arm uc_mcontext64_arm();
    }

    @Platforms({Platform.IOS_AARCH64.class})
    @CStruct(value = "__darwin_mcontext64", addStructKeyword = true)
    public interface MContext64Arm extends PointerBase {
        // https://github.com/xybp888/iOS-SDKs/blob/master/iPhoneOS11.2.sdk/usr/include/sys/cdefs.h#L440
        /**
         * Because the __LP64__ environment only supports UNIX03 semantics it causes __DARWIN_UNIX03 to be
         * defined, but causes __DARWIN_ALIAS to do no symbol mangling. (c)
         * */
        // https://github.com/xybp888/iOS-SDKs/blob/master/iPhoneOS11.2.sdk/usr/include/arm/_mcontext.h#L61
        /**
         * *  #define _STRUCT_MCONTEXT64      struct __darwin_mcontext64
         * * _STRUCT_MCONTEXT64
         * *  {
         * *     _STRUCT_ARM_EXCEPTION_STATE64   __es;
         * *     _STRUCT_ARM_THREAD_STATE64      __ss;
         * *     _STRUCT_ARM_NEON_STATE64        __ns;
         * * };
        */
        // https://github.com/xybp888/iOS-SDKs/blob/master/iPhoneOS11.2.sdk/usr/include/mach/arm/_structs.h#L96
        /**
         * #if __DARWIN_UNIX03
         * #define _STRUCT_ARM_THREAD_STATE64	struct __darwin_arm_thread_state64
         * _STRUCT_ARM_THREAD_STATE64
         * {
         * 	 __uint64_t __x[29];  // General purpose registers x0-x28
         *   __uint64_t __fp;        // Frame pointer x29
         *   __uint64_t __lr;        // Link register x30
         *   __uint64_t __sp;        // Stack pointer x31
         *   __uint64_t __pc;        // Program counter
         *   __uint32_t __cpsr;      // Current program status register
         *   __uint32_t __pad;       // Same size for 32-bit or 64-bit clients
         * };
         * */
        @CFieldAddress
        DarwinGregsPointer regs();

        @CFieldOffset("__ss.__fp")
        int fp_offset();

        @CFieldOffset("__ss.__lr")
        int lr_offset();

        @CFieldOffset("__ss.__sp")
        int sp_offset();

        @CFieldOffset("__ss.__pc")
        int pc_offset();

        @CFieldOffset("__ss.__cpsr")
        int cpsr_offset();

        @CFieldOffset("__ss.__pad")
        int pad_offset();
    }

    @Platforms({Platform.DARWIN_AMD64.class, Platform.IOS_AMD64.class})
    @CStruct(value = "__darwin_mcontext64", addStructKeyword = true)
    public interface MContext64 extends PointerBase {

        @CFieldOffset("__ss.__rax")
        int rax_offset();

        @CFieldOffset("__ss.__rbx")
        int rbx_offset();

        @CFieldOffset("__ss.__rip")
        int rip_offset();

        @CFieldOffset("__ss.__rsp")
        int rsp_offset();

        @CFieldOffset("__ss.__rcx")
        int rcx_offset();

        @CFieldOffset("__ss.__rdx")
        int rdx_offset();

        @CFieldOffset("__ss.__rbp")
        int rbp_offset();

        @CFieldOffset("__ss.__rsi")
        int rsi_offset();

        @CFieldOffset("__ss.__rdi")
        int rdi_offset();

        @CFieldOffset("__ss.__r8")
        int r8_offset();

        @CFieldOffset("__ss.__r9")
        int r9_offset();

        @CFieldOffset("__ss.__r10")
        int r10_offset();

        @CFieldOffset("__ss.__r11")
        int r11_offset();

        @CFieldOffset("__ss.__r12")
        int r12_offset();

        @CFieldOffset("__ss.__r13")
        int r13_offset();

        @CFieldOffset("__ss.__r14")
        int r14_offset();

        @CFieldOffset("__ss.__r15")
        int r15_offset();

        @CFieldOffset("__ss.__rflags")
        int efl_offset();
    }

    @CStruct
    @Platforms({Platform.LINUX_AARCH64.class, Platform.ANDROID_AARCH64.class})
    public interface mcontext_t extends PointerBase {
        // https://code.woboq.org/userspace/glibc/sysdeps/unix/sysv/linux/aarch64/sys/ucontext.h.html
        @CField
        long fault_address();

        @CFieldAddress
        GregsPointer regs();

        @CField
        long sp();

        @CField
        long pc();

        @CField
        long pstate();
    }

    public interface AdvancedSignalDispatcher extends CFunctionPointer {
        @InvokeCFunctionPointer
        void dispatch(int signum, siginfo_t siginfo, WordPointer opaque);
    }

    @CConstant
    public static native int SA_RESTART();

    @CConstant
    public static native int SA_SIGINFO();

    @CConstant
    public static native int SA_NODEFER();

    @CStruct(addStructKeyword = true)
    public interface sigaction extends PointerBase {
        @CField
        SignalDispatcher sa_handler();

        @CField
        void sa_handler(SignalDispatcher value);

        @CField
        AdvancedSignalDispatcher sa_sigaction();

        @CField
        void sa_sigaction(AdvancedSignalDispatcher value);

        @CField
        int sa_flags();

        @CField
        void sa_flags(int value);

        @CFieldAddress
        sigset_tPointer sa_mask();
    }

    /** @param signum from {@link SignalEnum#getCValue()} */
    @CFunction
    public static native int sigaction(int signum, sigaction act, sigaction oldact);

    @CEnum
    @CContext(PosixDirectives.class)
    public enum SignalEnum {
        SIGABRT,
        SIGALRM,
        SIGBUS,
        SIGCHLD,
        SIGCONT,
        SIGFPE,
        SIGHUP,
        SIGILL,
        SIGINT,
        SIGIO,
        SIGIOT,
        SIGKILL,
        SIGPIPE,
        SIGPROF,
        SIGQUIT,
        SIGSEGV,
        SIGSTOP,
        SIGSYS,
        SIGTERM,
        SIGTRAP,
        SIGTSTP,
        SIGTTIN,
        SIGTTOU,
        SIGURG,
        SIGUSR1,
        SIGUSR2,
        SIGVTALRM,
        SIGWINCH,
        SIGXCPU,
        SIGXFSZ;

        @CEnumValue
        public native int getCValue();
    }

    @Platforms(Platform.LINUX.class)
    @CEnum
    @CContext(PosixDirectives.class)
    public enum LinuxSignalEnum {
        SIGPOLL,
        SIGPWR;

        @CEnumValue
        public native int getCValue();
    }

    @Platforms(Platform.DARWIN.class)
    @CEnum
    @CContext(PosixDirectives.class)
    public enum DarwinSignalEnum {
        SIGINFO,
        SIGEMT;

        @CEnumValue
        public native int getCValue();
    }

    @CFunction
    public static native int sigemptyset(sigset_tPointer set);

    @CFunction
    public static native int sigaddset(sigset_tPointer set, int signum);
}
