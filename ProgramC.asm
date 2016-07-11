.code
test:
lw $a0,1($zero)
addi $t0,$t0,3
addi $t0,$t0,4
addi $v0,$zero,10
syscall
.data
dw 1
dw 2
dw 3
dw 4
dw 5