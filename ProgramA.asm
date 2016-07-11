.code
addi $a0,$a0,10
test:
addi $v0,$zero,20
addi $v1,$zero,3
syscall
addi $t0,$zero,10
addi $v0,$zero,10
syscall
.data
dw 11
dw 12
dw 13
dw 14
dw 15