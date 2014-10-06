.386p

MODEL   FLAT,C
IDEAL

b equ byte ptr
w equ word ptr
d equ dword ptr

FALSE = 0
TRUE =1

PUBLIC _AllocDosMem
PUBLIC _FreeDosMem

PUBLIC REGS_EDI
PUBLIC REGS_DI
PUBLIC REGS_ESI
PUBLIC REGS_SI
PUBLIC REGS_EBP
PUBLIC REGS_BP
PUBLIC REGS_EBX
PUBLIC REGS_BX
PUBLIC REGS_BL
PUBLIC REGS_BH
PUBLIC REGS_EDX
PUBLIC REGS_DX
PUBLIC REGS_DL
PUBLIC REGS_DH
PUBLIC REGS_ECX
PUBLIC REGS_CX
PUBLIC REGS_CL
PUBLIC REGS_CH
PUBLIC REGS_EAX
PUBLIC REGS_AX
PUBLIC REGS_AL
PUBLIC REGS_AH
PUBLIC REGS_FLAGS
PUBLIC _ES
PUBLIC _DS
PUBLIC _FS
PUBLIC _GS
PUBLIC REGS_IP
PUBLIC REGS_CS
PUBLIC REGS_SP
PUBLIC REGS_SS
CODESEG

align 4
label   regs_edi  dword
regs_di           dw      ?,?
label   regs_esi  dword
regs_si           dw      ?,?
Label   regs_ebp  dword
regs_bp           dw      ?,?
                  dd      ?
label   regs_ebx  dword
label   regs_bx   word
regs_bl           db      ?
regs_bh           db      ?,?,?
label   regs_edx  dword
label   regs_dx   word
regs_dl           db      ?
regs_dh           db      ?,?,?
label   regs_ecx  dword
label   regs_cx   word
regs_cl           db      ?
regs_ch           db      ?,?,?
label   regs_eax  dword
label   regs_ax   word
regs_al           db      ?
regs_ah           db      ?,?,?
regs_flags        dw      ?
_es               dw      ?
_ds               dw      ?
_fs               dw      ?
_gs               dw      ?
regs_ip           dw      ?
regs_cs           dw      ?
regs_sp           dw      ?
regs_ss           dw      ?

;*******************************************************************************
;* in:   nbr de paragraphe … alouer
;* out:  carry=1 si erreur
;*       carry=0 pas erreur et bx=protected mode selector for memory block
;*                          cx= real mode segment address
;*******************************************************************************
PROC _AllocDosMem C Nbr_Paragraphe:word
                        pushad
                        mov     eax,100h
                        mov     bx,[Nbr_Paragraphe]
                        int     31h
                        jc      short @@AllocDosMemError
                        mov     [dos_seg],ax
                        mov     [Pmode_selector],dx
                        popad
                        cld
                        mov     cx,[dos_seg]
                        mov     bx,[Pmode_selector]
                        ret
@@AllocDosMemError:     popad
                        std
                        ret
dos_seg        dw      ?
Pmode_selector dw      ?
ENDP

;*******************************************************************************
;* in:   Pmode selecteur
;* out:
;*******************************************************************************
PROC _FreeDosMem C Pmode_Selecteur:word
                        pushad
                        mov     eax,101h
                        mov     dx,[Pmode_selecteur]
                        int     31h
                        ret
ENDP

END
