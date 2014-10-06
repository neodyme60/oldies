;þþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþ
;þþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþ
;**   v1.0   Gestionnaire  VESA 2.0 & <2.0  NEODYME/MACLE
;þþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþ
;þþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþ
.586p
.MODEL FLAT,C
IDEAL

b       equ       byte ptr
w       equ       word ptr
d       equ       dword ptr
FALSE   =       0
TRUE    =       1

VESA2   =       1
VESA1   =       0


include "..\pmod\_pmod.inc"

PUBLIC Detect_Vesa     ;INIT_VESA
PUBLIC TEST_VESA_MODE
PUBLIC SET_VESA_MODE
PUBLIC OPEN_LFB
PUBLIC CLOSE_LFB
PUBLIC Off_Video_Hardware
PUBLIC CHANGE_VIDEO_BANK
PUBLIC TEST_ALL_VESA_MODE
PUBLIC VESA_MODE_INFO_PTR
PUBLIC VIDEO_MODE_NUMBER

PUBLIC VESA_8B_COPY_TO_SCREEN
PUBLIC VESA_16B_COPY_TO_SCREEN
PUBLIC VESA_32B_COPY_TO_SCREEN
;*****vars
PUBLIC X_rezo
PUBLIC Y_rezo
PUBLIC VIDEO_MODE
;***************************pour le vesa
PUBLIC Vbe_Signature
PUBLIC Vbe_Version
PUBLIC OemStringPtr
PUBLIC Capabilities
PUBLIC VideoModePtr
PUBLIC TotalMemory
PUBLIC OemSoftwareRev
PUBLIC OemVendorNamePtr
PUBLIC OemProductNamePtr
PUBLIC OemProductRevPtr
PUBLIC Reserved_vbe12
PUBLIC OemData
PUBLIC Mode_Attributes
PUBLIC WinA_Attributes
PUBLIC WinB_Attributes
PUBLIC Win_Granularity
PUBLIC Win_Size
PUBLIC WinA_Segment
PUBLIC WinB_Segment
PUBLIC Win_FuncPtr
PUBLIC Bytes_Per_ScanLine
PUBLIC X_Resolution
PUBLIC Y_Resolution
PUBLIC X_CharSize
PUBLIC Y_CharSize
PUBLIC Number_Of_Planes
PUBLIC Bits_Per_Pixel
PUBLIC Number_Of_Banks
PUBLIC Memory_Model
PUBLIC Bank_Size
PUBLIC Number_Of_ImagePages
PUBLIC Reserved_Page_Function
PUBLIC Red_Mask_Size
PUBLIC Red_Field_Position
PUBLIC Green_Mask_Size
PUBLIC Green_Field_Position
PUBLIC Blue_Mask_Size
PUBLIC Blue_Field_Position
PUBLIC Rsvd_Mask_Size
PUBLIC Rsvd_Field_Position
PUBLIC Direct_Color_Mode_Info
PUBLIC BasePhys_video
PUBLIC Off_Screen_Mem_Offset
PUBLIC Off_Screen_Mem_Size
PUBLIC Reserved_vbe20
;****************************************************
CODESEG
;************************global variable
;**************vesa info
;the vbe_signature filled by 'VBE2' bcoz ?? ca force la detection
; du vbe2 ( c'est kon ce ke tu dis la) et pi vesa_info=512 bytes !!
;****************
Vbe_Signature           db  'VBE2'      ;{ VBE Signature }
Vbe_Version             dw  0           ;{ VBE Version }
OemStringPtr            dd  0           ;{ Pointer to OEM String }
Capabilities            db  4 dup (0)   ;{ Capabilities of graphics cont. }
VideoModePtr            dd  0           ;{ Pointer to Video Mode List }
TotalMemory             dw  0           ;{ Number of 64kb memory blocks Added for VBE 2.0 }
OemSoftwareRev          dw  0           ;{ VBE implementation Software revision }
OemVendorNamePtr        dd  0           ;{ Pointer to Vendor Name String }

OemProductNamePtr       dd  0           ;{ Pointer to Product Name String }
OemProductRevPtr        dd  0           ;{ Pointer to Product Revision String }
Reserved_vbe12          db  222 dup (0) ;{ Reserved for VBE implementation scratch area }
OemData                 db  256 dup (0) ;{ Data Area for OEM Strings }

;***************mode info
Mode_Attributes         dw  0           ;{ mode attributes }
WinA_Attributes         db  0           ;{ window A attributes }
WinB_Attributes         db  0           ;{ window B attributes }
Win_Granularity         dw  0           ;{ window granularity }
Win_Size                dw  0           ;{ window size }
WinA_Segment            dw  0           ;{ window A start segment }
WinB_Segment            dw  0           ;{ window B start segment }
Win_FuncPtr             dd  0           ;{ pointer to window function }
Bytes_Per_ScanLine      dw  0           ;{ bytes per scan line }

X_Resolution            dw  0           ;{ horizontal resolution in pixels or chars }
Y_Resolution            dw  0           ;{ vertical resolution in pixels or chars }
X_CharSize              db  0           ;{ character cell width in pixels }
Y_CharSize              db  0           ;{ character cell height in pixels }
Number_Of_Planes        db  0           ;{ number of memory planes }
Bits_Per_Pixel          db  0           ;{ bits per pixel }
Number_Of_Banks         db  0           ;{ number of banks }
Memory_Model            db  0           ;{ memory model type }
Bank_Size               db  0           ;{ bank size in KB }
Number_Of_ImagePages    db  0           ;{ number of images }
Reserved_Page_Function  db  0           ;{ reserved for page function }

Red_Mask_Size           db  0           ;{ size of direct color red mask in bits }
Red_Field_Position      db  0           ;{ bit position of lsb of red mask }
Green_Mask_Size         db  0           ;{ size of direct color green mask in bits }
Green_Field_Position    db  0           ;{ bit position of lsb of green mask }
Blue_Mask_Size          db  0           ;{ size of direct color blue mask in bits }
Blue_Field_Position     db  0           ;{ bit position of lsb of blue mask }
Rsvd_Mask_Size          db  0           ;{ size of direct color reserved mask in bits }
Rsvd_Field_Position     db  0           ;{ bit position of lsb of reserved mask }
Direct_Color_Mode_Info  db  0           ;{ direct color mode attributes }

BasePhys_video          dd  0           ;{ physical address for flat frame buffer }
Off_Screen_Mem_Offset   dd  0           ;{ pointer to start of off screen memory }
Off_Screen_Mem_Size     dw  0           ;{ amount of off screen memory in 1k units }
Reserved_vbe20          db  206 dup (0) ;{ remainder of ModeInfoBlock }
label END_MODE_INFO

;*****************************************
x_rezo                  dd      0
y_rezo                  dd      0

;***********************private variable
seg_mem                 dw 0
off_mem			dw 0
pal_convertion1         dw 256 dup(0)         ;pal pour 256c->65536&32768
erreur                  db 0
;*****************************************
VESA_MODE_INFO:
        rept    200
        dw      0,0,0,0,0
        ENDM
VESA_MODE_INFO_PTR        dd      offset VESA_MODE_INFO

;**********************public variable
VESA_TYPE       dd      ?
VIDEO_MODE      dd      0

;*******************************************************************************
;* in: /
;* out: eax=1 if vesa detected & vesa info is filled
;*              !!! les oem...ptr et videomodeptr sont 32bits :-)
;*      eax=0 if vesa  not detected 
;*******************************************************************************
PROC                    Detect_Vesa     C USES EBX ECX ESI EDI

                        call    _allocdosmem c,512/16               ;alloc dos mem
                        mov     [real_mode_segment_address],cx
                        mov     [pmode_selector_for_memory_block],bx

                        mov     [w _es],cx
                        mov     edi,0                               ;offset nul
                        mov     ax,4F00h
                        mov     [d Vbe_Signature],'2EBV'            ;'force vbe2
                        call    int_10h
                        cmp     ax,004Fh                            ;' Vesa Supported ?!!
                        je      short @@Vesa_detected
                        mov     [d Vbe_Signature],'2EBV'            ;'force vbe2
                        call    _freedosmem c,[w pmode_selector_for_memory_block] ;free dos mem
                        mov     eax,0	;try univbeuuuhh 
                        ret

@@Vesa_detected:        xor     esi,esi                             ;
                        mov     si,[real_mode_segment_address]      ;on recopie tout plus
                        shl     esi,4                               ;
                        mov     edi,offset Vbe_Signature            ;haut
                        mov     ecx,512  ;info + oem string         ;
                        cld                                         ;
                        rep     movsb                               ;

;////FUCKING REAL MODE
;***kalcul offset de translation en ECX
                        mov     ecx,offset Vbe_Signature
                        mov     ebx,0
                        mov     bx,[real_mode_segment_address]
                        shl     ebx,4
                        sub     ecx,ebx                 ;offset of translation

                        call    translate c,[d OemStringPtr]
                        mov     [d OemStringPtr],eax

                        call    translate c,[d VideoModePtr]
                        mov     [d VideoModePtr],eax

                        call    translate c,[d OemVendorNamePtr]
                        mov     [d OemVendorNamePtr],eax

                        call    translate c,[d OemProductNamePtr]
                        mov     [d OemProductNamePtr],eax

                        call    translate c,[d OemProductRevPtr]
                        mov     [d OemProductRevPtr],eax

                        call    _freedosmem c,[w pmode_selector_for_memory_block] ;fre dos mem

                        mov     eax,1    ;no error  that's good yeahhh !!!
                        ret

real_mode_segment_address       dw      0
pmode_selector_for_memory_block dw      0
endp

;******************************************************************************
;*  in:  ..
;* out: eax=le code de retour de l'interuption 10h
;* 	eax=0  alors tout c'est bien passer sinon eax= code de l'erreur
;******************************************************************************
PROC                    int_10h           near    C
                        mov     [d regs_ebx],ebx
                        mov     [d regs_eax],eax     ;reel (coupure de zik!) et en
                        mov     [d regs_ecx],ecx     ;mettant les regs avec leur valeurs
                        mov     [d regs_edx],edx     ;en retour...
                        mov     [d regs_esi],esi
                        mov     [d regs_edi],edi
                        mov     [d regs_ebp],ebp
                        mov     [w regs_ss],0
                        mov     [w regs_sp],0
                        xor     cx,cx
                        mov     edi,offset regs_edi
                        mov     bx,10h
                        mov     ax,300h
;                        push    ds es
                        int     31h
;                        pop     es ds
                        mov     ax,[w regs_flags]
                        and     ax,1
                        cmp     ax,0
                        jne     short no_Error_Vesa
                        mov     eax,[d regs_eax]
                        mov     ebx,[d regs_ebx]
                        mov     ecx,[d regs_ecx]
                        mov     edx,[d regs_edx]
                        mov     esi,[d regs_esi]
                        mov     edi,[d regs_edi]
                        mov     ebp,[d regs_ebp]

no_error_vesa:          ret

endp
;******************************************************************************
;*  in:  ..
;* out: eax=0 if LFB is open
;* out: eax=1 if LFB is not open
;******************************************************************************
PROC                    Open_LFB       C USES  EBX ECX EDX ESI EDI 

                        mov     ax,ds
                        mov     es,ax
                        mov     ecx,[d BasePhys_video]
                        movzx   edi,[w TotalMemory] ;alloc all video carte mem
                        shl     edi,16
                        shld    ebx,ecx,16        ;'bx:cx adresse physique
                        shld    esi,edi,16        ;'si:di longueur voulue
                        mov     si,1dh
                        mov     di,4c00h
                        mov     eax,0800h
                        push    ds es
                        int     31h
                        pop     es ds
                        jc      short LFB_err
                        shl     ebx,16
                        mov     bx,cx             ;' ebx adresse lineaire
                        mov     [d Off_Video_Hardware],ebx
No_LFB_err:             mov     eax,0
                        ret
LFB_err:                mov     eax,1
			ret

Endp

Off_Video_Hardware      dd      0
;******************************************************************************
;*  in:  ..
;* out: eax=1 if LFB is close propely
;* out: eax=0 if LFB is not close propely
;******************************************************************************
PROC                    Close_LFB  c USES EBX ECX ESI EDI
                        mov     ax,ds
                        mov     es,ax
                        mov     bx,[w Off_Video_Hardware+2]
                        mov     cx,[w Off_Video_Hardware]
                        mov     ax,0801h
                        int     31h
                        jc      short CLOSE_LFB_err
NO_CLOSE_LFB_err:
                        mov     eax,0
                        ret

CLOSE_LFB_err:
                        mov     eax,1
                        ret
Endp
;*******************************************************************************
PROC                    CHANGE_VIDEO_BANK  c USES EAX EBX ECX EDX ESI EDI, PAGE_NUMBER:DWORD

                        mov     eax,[d PAGE_NUMBER]
                        shl     eax,6                 ;*64
                        movzx   ebx,[w Win_Granularity]
                        cmp     eax,0
                        je      bank0
                        xor     edx,edx
                        div     ebx

bank0:                  mov     edx,eax
                        mov     eax,4f05h
                        mov     ebx,0
                        int 10h

                        ret
endp
;*******************************************************************************
;*      in: v=rezo horizontal
;*          h=rezo vert
;*          p=bit per pixel
;*
;*      out:	eax=mode number
;*		eax=0 si pas trouver
;*******************************************************************************
PROC                    video_mode_number  c USES EBX ECX EDX ESI EDI, h:word,v:word,p:word
                        mov     esi,offset VESA_MODE_INFO
       @@lop:
                        xor     eax,eax
                        mov     ax,[esi]
                        cmp     ax,-1
                        je      short @@not_found
                        cmp     ax,[h]
                        jne     short @@next_mode

                        mov     ax,[esi+2]
                        cmp     ax,[v]
                        jne     short @@next_mode

                        mov     ax,[esi+4]
                        cmp     ax,[p]
                        jne     short @@next_mode

                        mov     ax,[esi+6]
                        cwd
                        ret	;ici eax=le numero du mode video

      @@next_mode:      add     esi,8
                        jmp     short @@lop

@@not_found:            mov     eax,0
			ret
ENDP

;*******************************************************************************
;*
;*in:	rien
;*out: rien
;*******************************************************************************
PROC                    TEST_ALL_VESA_MODE c USES  EBX ECX ESI EDI

                        mov     esi,[VideoModePtr]
                        mov     edi,offset VESA_MODE_INFO
                        xor     eax,eax

                 @@lop:
                        mov     ecx,0
                        mov     cx,[esi]

                        call    Test_Vesa_Mode   c,ecx

                        mov     bx,[X_Resolution]
                        mov     [edi],bx

                        mov     bx,[Y_Resolution]
                        mov     [edi+2],bx

                        xor     bx,bx
                        mov     bl,[Bits_Per_Pixel]
                        mov     [edi+4],bx

                        mov     [edi+6],cx

                        add     esi,2
                        add     edi,8

                        cmp     ax,1
                        jne     short @@lop

                        ret
ENDP

;*******************************************************************************
;*
;*  in :  video mode number
;*  return : eax=1  if mode supported   & mode info is filled
;*           eax=0  if mode not supported
;*******************************************************************************
PROC                    Test_Vesa_Mode  c USES EBX ECX EDX ESI EDI ES DS, MODE_NUMBER:dword

                        mov     ax,ds
                        mov     es,ax

                        call    _allocdosmem c,512/16              ;alloc dos mem
                        mov     [real_mode_segment_address],cx
                        mov     [pmode_selector_for_memory_block],bx

                        mov     ax,cx
                        mov     [w _es],ax

                        mov     ecx,[MODE_NUMBER]
                        mov     edi,0
                        mov     eax,4F01h
                        call    int_10h           ;'demande d information
                        cmp     ax,004Fh
                        je      short @@Valide
                        call    _freedosmem c,[w pmode_selector_for_memory_block] ;fre dos mem
                        mov     eax,1
                        ret

@@Valide:               xor     esi,esi                         ;
                        mov     si,[real_mode_segment_address]  ;on recopie tout plus
                        shl     esi,4                           ;
                        mov     edi,offset Mode_Attributes     ;512 bytes bcoz vbe2 !!
                        mov     ecx,512  ;info mode              ;
                        cld                                     ;
                        rep     movsb                           ;
                        call    _freedosmem c,[w pmode_selector_for_memory_block] ;fre dos mem
                        mov     eax,0
                        ret
Endp

;*******************************************************************************
;*
;*  in :  video mode number
;*  out:  eax=1 set if mode is set
;*        eax =0  if mode not set
;*******************************************************************************
PROC                    Set_Vesa_Mode  c  USES EBX ECX EDI ESI, ModeCmd:dword,VESA_TYP:dword

                        mov     ax,ds
                        mov     es,ax

                        mov     ebx,[VESA_TYP]
                        mov     [dword ptr VESA_TYPE],ebx


                        mov     ebx,[ModeCmd]
                        or      bx,1000000000000000b ; b15=0 => clear video
                        cmp     [dword ptr VESA_TYP],VESA2
                        je      short @@vesa2
                        cmp     [dword ptr VESA_TYP],VESA1
                        je      short @@vesa1
                        jmp     short @@Set_Vesa_Mode_err

               @@vesa1:
                        ;+ b14=0 => windowed memory
                        and     bx,1011111111111110b
                        jmp     short @@next
               @@vesa2:
                        ;+ b14=1 => flat  memory
                        or      bx,0100000000000000b
               @@next:
                        mov     eax,4F02h
                        call    int_10h
                        cmp     ax,004Fh
                        jne     short @@Set_Vesa_Mode_err

                        cmp     [dword ptr VESA_TYPE],VESA2
                        jne     short @@next2
                        call    OPEN_LFB

               @@next2:
                        mov     eax,0
                        ret

@@Set_Vesa_Mode_err:
                        mov     eax,1
			ret
Endp
;*******************************************************************************
;*        vesa(256c)-> screen
;*******************************************************************************
PROC                    Vesa_8b_copy_to_screen   c USES EBX ECX EDX EDI ESI, Buffer_ptr:dword
                        cld
                        xor     eax,eax
                        xor     ecx,ecx
                        xor     edx,edx
                        mov     ax,[X_Resolution]
                        mov     cx,[Y_Resolution]
                        mul     ecx

                        cmp     [byte ptr VESA_TYPE],VESA2
                        je      short @@vesa2
                        cmp     [byte ptr VESA_TYPE],VESA1
                        je      short @@vesa1
                        jmp     short @@Set_Vesa_copy_err

               @@vesa1:
                        mov     ecx,eax
                        shr     ecx,16
                        xor     edx,edx
                        mov     esi,[Buffer_ptr]

@@copy_each_bank:       call    CHANGE_VIDEO_BANK    c,edx
                        call    COPY c,esi,0a0000h,1024*64
                        add     esi,1024*64
                        inc     edx
                        loop    @@copy_each_bank
;a cause du rest qui n'est pas forcement de 64kb
                        call    change_video_bank c,edx
                        mov     edi,0a0000h
                        shl     edx,16
                        sub     eax,edx
                        call    COPY c,esi,0a0000h,eax
                        jmp     short @@fin
               @@vesa2:
                        call    COPY c,[Buffer_ptr],[Off_Video_Hardware],eax

               @@fin:
                        mov     eax,0
                        ret

@@Set_Vesa_copy_err:
                        mov     eax,1
			ret
Endp

;*******************************************************************************
;*        vesa( Hicolor& Color) ->screen
;*******************************************************************************
PROC                    Vesa_16b_copy_to_screen c USES EBX ECX EDX ESI EDI, Buffer_ptr:dword
                        cld
                        xor     eax,eax
                        xor     ecx,ecx
                        xor     edx,edx
                        mov     ax,[X_Resolution]
                        mov     cx,[Y_Resolution]
                        mul     ecx

                        cmp     [byte ptr VESA_TYPE],VESA2
                        je      short @@vesa2
                        cmp     [byte ptr VESA_TYPE],VESA1
                        je      short @@vesa1
                        jmp     short @@Set_Vesa_copy_err

               @@vesa1:
                        shl     eax,1
                        mov     ecx,eax
                        shr     ecx,16
                        xor     edx,edx
                        mov     esi,[Buffer_ptr]

@@copy_each_bank:       call    CHANGE_VIDEO_BANK    c,edx
                        call    COPY c,esi,0a0000h,1024*64
                        add     esi,1024*64
                        inc     edx
                        loop    short @@copy_each_bank

;a cause du rest qui n'est pas forcement de 64kb

                        call    change_video_bank c,edx
                        mov     edi,0a0000h
                        shl     edx,16
                        sub     eax,edx
                        call    COPY c,esi,0a0000h,eax

                        jmp     short @@fin
               @@vesa2:
                        shl     eax,1
                        call    COPY c,[Buffer_ptr],[Off_Video_Hardware],eax
               @@fin:
                        mov     eax,0
                        ret

@@Set_Vesa_copy_err:
                        mov     eax,1
			ret
ENDP
;*******************************************************************************
;*        vesa( Hicolor& Color) ->screen
;*******************************************************************************
PROC                    Vesa_32b_copy_to_screen c USES EBX ECX ESI EDI, Buffer_ptr:dword
                        cld
                        xor     eax,eax
                        xor     ecx,ecx
                        xor     edx,edx
                        mov     ax,[X_Resolution]
                        mov     cx,[Y_Resolution]
                        mul     ecx
                        shl     eax,2

                        cmp     [byte ptr VESA_TYPE],VESA2
                        je      short @@vesa2
                        cmp     [byte ptr VESA_TYPE],VESA1
                        je      short @@vesa1
                        jmp     short @@Set_Vesa_copy_err

               @@vesa1: 
                        mov     ecx,eax
                        shr     ecx,16
                        xor     edx,edx
                        mov     esi,[Buffer_ptr]

@@copy_each_bank:       call    CHANGE_VIDEO_BANK    c,edx
                        call    COPY c,esi,0a0000h,1024*64
                        add     esi,1024*64
                        inc     edx
                        loop    short @@copy_each_bank

;a cause du rest qui n'est pas forcement de 64kb

                        call    change_video_bank c,edx
                        mov     edi,0a0000h
                        shl     edx,16
                        sub     eax,edx
                        call    COPY c,esi,0a0000h,eax

                        jmp     short @@fin
               @@vesa2:
                        call    COPY c,[Buffer_ptr],[Off_Video_Hardware],eax
               @@fin:
                        mov     eax,0
                        ret

@@Set_Vesa_copy_err:
                        mov     eax,1
			ret
ENDP
;**********************************************************************
;* esi=source  edi=destination  ecx=size in byte
;**********************************************************************
proc            COPY    c USES EBX ECX EDX ESI EDI DS ES, source_ptr:dword,destination_ptr:dword,size_in_byte:dword
                push ds
                pop es
                mov     esi,[source_ptr]
                mov     edi,[destination_ptr]
                mov     ecx,[size_in_byte]
                cld
                align   4
                shr     ecx,1
                jnc     short @@COPY_WORD
                movsb
@@COPY_WORD:    shr     ecx,1
                jnc     short @@COPY_DWORD
                movsw
@@COPY_DWORD:   rep movsd
                RET
ENDp
;*******************************************************************************
;*  translate un ptr reel -> protected mode
;*  in:  en parametre adre reel seg:offset
;*  out: adr 32bit en eax
;*******************************************************************************
proc                    translate USES EBX ECX EDX EDI ESI,  real_ptr:dword
                        mov     ebx,0
                        mov     eax,[real_ptr]
                        shr     eax,16
                        cmp     ax,[w real_mode_segment_address]
                        jne     short @@IN_ROM1
      @@IN_RAM1:        add     ebx,ecx
      @@IN_ROM1:
                        shl     eax,4
                        mov     edx,[real_ptr]
                        and     edx,0ffffh
                        add     eax,edx
                        add     eax,ebx
                        mov     [real_ptr],eax
                        mov     eax,[real_ptr]          ;32bit ptr
                        ret
endp


END
