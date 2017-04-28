.data
__newline__:	.asciiz	"\n"
__input__:	.asciiz	"input: "
fee:	.word	0
fi:	.word	0
fo:	.word	0
fum:	.word	0

.text
main:

#Push to stack
addi	$sp,	$sp,	-40
sw	$s7,	36($sp)
sw	$s6,	32($sp)
sw	$s5,	28($sp)
sw	$s4,	24($sp)
sw	$s3,	20($sp)
sw	$s2,	16($sp)
sw	$s1,	12($sp)
sw	$s0,	8($sp)
sw	$fp,	4($sp)
sw	$ra,	0($sp)

#Assignment

#Expression
li	$s0,	4
sw	$s0,	fee

#Assignment

#Expression
li	$s0,	5
sw	$s0,	fi

#Assignment

#Expression

#Expression

#Expression
li	$s1,	3

#Expression
lw	$s2,	fee
mult	$s1,	$s2
mflo	$s0

#Expression
lw	$s1,	fi
add	$s0,	$s0,	$s1
sw	$s0,	fo

#If statement

#Expression
lw	$s0,	fo

#Expression
li	$s1,	13
bge	$s0,	$s1,	else0

# then

#Assignment

#Expression
li	$s0,	13
sw	$s0,	fo
j	endIf0

# else
else0:

#Assignment

#Expression
li	$s1,	26
sw	$s1,	fo
endIf0:

#Restore from stack
lw	$s7,	36($sp)
lw	$s6,	32($sp)
lw	$s5,	28($sp)
lw	$s4,	24($sp)
lw	$s3,	20($sp)
lw	$s2,	16($sp)
lw	$s1,	12($sp)
lw	$s0,	8($sp)
lw	$fp,	4($sp)
lw	$ra,	0($sp)
addi	$sp,	$sp,	40
jr	$ra
