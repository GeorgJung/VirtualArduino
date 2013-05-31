.org 0x0000
rjmp main
 
main:
ldi r16, 0xFF
out DDRB, r16
loop:
sbi PortB, 3
cbi PortB, 3
rjmp loop