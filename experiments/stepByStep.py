#!/bin/env python3

"""
Simulate kloc "processor" step by step.
Can be used to play with memory and loops
(robot commands - go, lt, rt - do nothing).

Simple, interactive cl application.
It starts in LOAD state: enter code
to be simulated as single line of text.

Then, control simulator entering one of following:
  'r' - RESET - resets kloc processor;
  'i' - INPUT - put value into input register,
                should be followed by line with integer number;
  'l' - LOAD  - stop simulation and go back to entering program,
                should be followed by line with code
   anything else  - step kloc 'processor' and display its state.
   
"""

from kloc import *

if __name__ == "__main__":
    cpu = Cepeu()
    try:
        
        while True:
            print("(Program<)")
            code = input()
            cpu.load(code)
            print(cpu)
            
            while True:
                try:
                    cmd = input()
                    if cmd == "r":
                        print("[RESET]")
                        cpu.reset()
                        print(cpu)
                    elif cmd == "i":
                        cpu.setIn(int(input()))
                    elif cmd == "l":
                        break
                    else:
                        cpu.step()
                        print(cpu)
                except EndInterrupt as end:
                    print(cpu)
                    print(end)
                    break
                except CepeuInterrupt as e:
                    print(e)
                    break
                    
    except KeyboardInterrupt:
        print("Bye")
    except EOFError:
        print("Bye")

