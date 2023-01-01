"""
Kloc "processor".

Similar to brainfuck but with movement
commands.
"""

class Asm:
    """
    All supported operations.
    """
    ad1 = "+1"  # increment memory value
    ad2 = "+2"  # add 2 to memory value
    ad4 = "+4"  # add 4 to memory value
    sub = "-1"  # decrement memory value
    mup = "m+"  # move memory pointer forward
    mdn = "m-"  # move memory pointer backward
    go  = "go"  # move robot forward (does nothing here)
    lt  = "lt"  # turn robot left (does nothing here)
    rt  = "rt"  # turn robot right (does nothing here)
    jz  = "["   # if memory value equals zero, jump after matching ']'
    zj  = "]"   # if memory value is not zero, jump to matching '['
    inp = "in"  # copy value from input register to memory
    out = "out" # copy value from memory to output register

class CepeuInterrupt(Exception):
    """
    Base class for Kloc processor exception.
    """
    def __init__(self, msg = ""):
        Exception.__init__(self, msg)
        
class EndInterrupt(CepeuInterrupt):
    """
    Program ended interrupt.
    """
    def __init__(self, pc):
        CepeuInterrupt.__init__(self, "Code execution finished with PC = " + str(pc))
        
class InvalidOpInterrupt(CepeuInterrupt):
    """
    Invalid operation error.
    """
    def __init__(self, pc, op):
        CepeuInterrupt.__init__(self, "Invalid OP \"%s\" @pc = %d" % (op, pc))
        
class InvalidMemAccess(CepeuInterrupt):
    """
    Invalid memory access error.
    """
    def __init__(self, pc, ptr):
        CepeuInterrupt.__init__(self, "Invalid memory access %d @pc = %d" % (ptr, pc))
    
class NumericOverflow(CepeuInterrupt):
    """
    Numeric overflow error.
    """
    def __init__(self, pc, val):
        CepeuInterrupt.__init__(self, "Numeric overflow %d @pc = %d" % (val, pc))
        
        
def go(cpu):
    """
    Placeholder for robot-go command.
    """
    pass
    
def left(cpu):
    """
    Placeholder for robot-turn-left command.
    """
    pass
    
def right(cpu):
    """
    Placeholder for robot-turn-right command.
    """
    pass
    
class Cepeu:
    """
    Kloc processor simulator.
    """
    
    # max 30kb of code - like in keil :)
    PC_MAX = 30 * 1024
    MEM_MAX = 10 * 1024
    
    def __init__(self):
        self.code = []
        self.reset()
        
    def reset(self):
        self.ptr = 0
        self.mem = {}
        self.pc = 0
        self.input = 0
        self.output = 0
        self.memwindow = - 10
        self.codewindow = 0
        
    def load(self, code):
        self.code = code.split()
        self.reset()
        
    def setIn(self, value):
        self.input = value
        
    def getOut(self):
        return self.output
        
    def step(self):
        op = self._asm()
        fun = {
            Asm.ad1 : Cepeu.addOne,
            Asm.ad2 : Cepeu.addTwo,
            Asm.ad4 : Cepeu.addFour,
            Asm.sub : Cepeu.subOne,
            Asm.mup : Cepeu.memoryPointerInc,
            Asm.mdn : Cepeu.memoryPointerDec,
            Asm.go  : go,
            Asm.lt  : left,
            Asm.rt  : right,
            Asm.jz  : Cepeu.jumpz,
            Asm.zj  : Cepeu.jumpnz,
            Asm.inp : Cepeu.input,
            Asm.out : Cepeu.output,
        }.get(op)
        if None == fun:
            raise InvalidOpInterrupt(self.pc, op)
        fun(self)
        self._incPc()
            
    def addOne(self):
        self._addReg(1)
        
    def addTwo(self):
        self._addReg(2)
        
    def addFour(self):
        self._addReg(4)
        
    def subOne(self):
        self._decReg()
        
    def memoryPointerInc(self):   
        self._incPtr()
        
    def memoryPointerDec(self):
        self._decPtr()
        
    def jumpz(self):
        if (0 == self.mem.get(self.ptr, 0)):
            braces = 1
            while braces > 0:
                self._incPc()
                if self.code[self.pc] == Asm.zj:
                    braces -= 1
                elif self.code[self.pc] == Asm.jz:
                    braces += 1
        
    def jumpnz(self):  
        if (0 != self.mem.get(self.ptr, 0)):
            braces = 1
            while braces > 0:
                self._decPc()
                if self.code[self.pc] == Asm.jz:
                    braces -= 1
                elif self.code[self.pc] == Asm.zj:
                    braces += 1
                    
        
    def input(self):
        if self.input > 32767 or self.input < -32768:
            raise NumericOverflow(self.pc, self.input)
        self.mem[self.ptr] = self.input
        
    def output(self):
        self.output = self.mem.get(self.ptr, 0)
            

    def _incPc(self):
        self.pc += 1
        if self.pc >= len(self.code):
            raise EndInterrupt(self.pc)
    
    def _decPc(self):
        self.pc -= 1
        if self.pc < 0:
            raise EndInterrupt(self.pc)
            
    def _addReg(self, val):
        newreg = self.mem.get(self.ptr, 0) + val
        if newreg > 32767:
            raise NumericOverflow(self.pc, newreg)
        self.mem[self.ptr] = newreg
        
    def _decReg(self):
        newreg = self.mem.get(self.ptr, 0) - 1
        if newreg < -32768:
            raise NumericOverflow(self.pc, newreg)
        self.mem[self.ptr] = newreg
        
    def _incPtr(self):
        self.ptr += 1
        if self.ptr > 32767:
            raise InvalidMemAccess(self.pc, self.ptr)
            
    def _decPtr(self):
        self.ptr -= 1
        if self.ptr < -32768:
            raise InvalidMemAccess(self.pc, self.ptr)
        
    def _asm(self):
        if self.pc < 0 or self.pc >= len(self.code):
            raise EndInterrupt(self.pc)
        return self.code[self.pc]
        
    def _getAsm(self, idx):
        if idx < 0 or idx >= len(self.code):
            return ""
        return self.code[idx][:3]
        
    def __str__(self):
        while self.ptr - 1 < self.memwindow:
            self.memwindow -= 2
        while self.ptr + 1 > self.memwindow + 10:
            self.memwindow += 2
            
        while self.pc - 1 < self.codewindow:
            self.codewindow -= 2
        while self.pc + 1 > self.codewindow + 20:
            self.codewindow += 2
    
        txt = "MEM[%d]: " % self.ptr
        txt += " ".join(("[%d]" if i == self.ptr else "%d") % self.mem.get(i, 0) \
                    for i in range(self.memwindow, self.memwindow + 20)) + "\n"
        txt += "IN:  %d\n" % self.input
        txt += "OUT: %d\n" % self.output
        txt += "\n"
        txt += " ".join("%3s" % self._getAsm(i) for i in range(self.codewindow, self.codewindow + 20)) + "\n"
        txt += "    " * (self.pc - self.codewindow)
        txt += " ^\n"
        return txt
        
if __name__ == "__main__":
    uut = Cepeu()
    uut.load("go")
    print(uut)
    