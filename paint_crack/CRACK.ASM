b equ byte ptr
w equ word ptr
l equ dword ptr

x_rezo equ 320
y_rezo equ 200

.486
;.587
ideal
model tiny
codeseg

;þþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþ
start:
        push    @data
        pop     ds

;        mov     ax,es           ;libere la memoire non utilise
;        mov     bx,ss           ;
;        sub     bx,ax           ;
;        add     bx,4096         ;
;        mov     ah,4ah          ;
;        int     21h             ;

        mov     dx,seg msg2
        mov     ds,dx
        mov     dx,offset msg2
        mov     ax,0900h                ;ds:dx =msg
        int     21h

        ;*open file
        mov     ax,3d02h                   ;open file
        mov     dx,seg  file
        mov     ds,dx
        mov     dx,offset file
        int     21h
        mov     [_LOAD_DATA_HANDLE],eax

        jnc     SEEK
        mov     dx,seg error1
        mov     ds,dx
        mov     dx,offset error1
        jmp     error
SEEK:

        ;*seek
        mov     eax,4200h                   ;positionnement dans le fichier
        mov     ebx,[_LOAD_DATA_HANDLE]
        mov     ecx,[seek_pos]
        mov     edx,ecx
        shr     ecx,16
        and     ecx,0ffffh
        and     edx,0ffffh
        int     21h

        jnc     WRITE
        mov     dx,seg error2
        mov     ds,dx
        mov     dx,offset error2
        jmp     error


WRITE:
        ;*write
        mov     eax,4000h                   ;positionnement dans le fichier
        mov     ebx,[_LOAD_DATA_HANDLE]
        mov     dx,seg patch
        mov     ds,dx
        mov     dx,offset patch
        mov     cx,1
        int     21h

        jnc     CLOSE
        mov     dx,seg error3
        mov     ds,dx
        mov     dx,offset error3
        jmp     error

CLOSE:
        ;* close
        mov     eax,3e00h                   ;close file access
        mov     ebx,[_LOAD_DATA_HANDLE]
        int     21h

        mov     dx,seg msg3
        mov     ds,dx
        mov     dx,offset msg3
        mov     ax,0900h                ;ds:dx =msg
        int     21h

        jmp     the_end
error:
        mov     ax,0900h                ;ds:dx =msg
        int     21h

the_end:

        mov     ax,4c00h
        int     21h         ;The end |-<

;þþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþþ
;//////////////////////////////////////////////////

seek_pos        dd      01c8c6h
error1          db      'ERROR:   file not found',0,'$',0
error2          db      13,10,'ERROR:   seek file','$',0
error3          db      13,10,'ERROR:   write filt','$',0
msg1            db      13,10,'file open','$',0
msg2            db      13,10,'Crack paint.exe v2.1.1 by zoby the fly','$'
msg3            db      13,10,'crack done','$',0
file            db      'paint.exe',0
patch           db      74h

_LOAD_DATA_HANDLE       dd      ?

end start
