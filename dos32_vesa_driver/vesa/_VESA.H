//***********************************************************
//*  v1.0 NEODYME/MACLE
//***********************************************************

    #ifndef VESA_H
    #define VESA_H


//**********************************************************
//                       procedure
//**********************************************************

    #ifndef ASM_TYPE_H
    #define ASM_TYPE_H
    typedef unsigned    int     ULONG;      //32 bit unsigned.
    typedef unsigned    short   UWORD;      //16 bit unsigned.
    typedef unsigned    char    UCHAR;      //16 bit unsigned.
    #endif

    #define  VESA2          1
    #define  VESA1          0

//***
    extern  void        __cdecl VSYNC();
    extern  void        __cdecl HSYNC1();
    extern  void        __cdecl HSYNC2();
//    extern  void        __cdecl LOADPAL(char start_color,
//                                        char end_color,
//                                        char *ptr_pal);
//***

//*** pour le vesa
    extern   int        __cdecl DETECT_VESA();
    extern   int        __cdecl TEST_VESA_MODE(int);
    extern   int        __cdecl SET_VESA_MODE(int,int);
    extern   void       __cdecl CHANGE_VIDEO_BANK(int);
    extern   void       __cdecl TEST_ALL_VESA_MODE();
    extern   int        __cdecl VESA_MODE_INFO_PTR;  // short *
    extern   int        __cdecl VIDEO_MODE_NUMBER(short ,short,short);

//**** pour le vesa2
    extern   void       __cdecl OPEN_LFB();
    extern   void       __cdecl CLOSE_LFB();

//**** copy to screen
    extern  void        __cdecl VESA_8B_COPY_TO_SCREEN(unsigned char *);
    extern  void        __cdecl VESA_16B_COPY_TO_SCREEN(unsigned short *);
    extern  void        __cdecl VESA_32B_COPY_TO_SCREEN(unsigned int *);

//**********************************************************
//                       variables
//**********************************************************
    extern   ULONG __cdecl Off_Video_Hardware;
//    extern   char _erreur;
    extern   ULONG _X_REZO;
    extern   ULONG _Y_REZO;
//    extern   unsigned char _VESA_TYPE;
    extern   int _VIDEO_MODE;

//*************************vbe rules
    extern   char Vbe_Signature[4];
    extern   short Vbe_Version;
    extern   char * OemStringPtr;
    extern   long Capabilities;       //?
    extern   short *VideoModePtr;
    extern   short TotalMemory;
    extern   short OemSoftwareRev;
    extern   char * OemVendorNamePtr;
    extern   char * OemProductNamePtr;
    extern   char * OemProductRevPtr;
    extern   char Reserved_vbe12[222];
    extern   char OemData[256];

    extern   short Mode_Attributes;
    extern   char WinA_Attributes;
    extern   char WinB_Attributes;
    extern   short Win_Granularity;
    extern   short Win_Size;
    extern   short WinA_Segment;
    extern   short WinB_Segment;
    extern   void * Win_FuncPtr;        //?
    extern   short Bytes_Per_ScanLine;
    extern   short X_Resolution;
    extern   short Y_Resolution;
    extern   char X_CharSize;
    extern   char Y_CharSize;
    extern   char Number_Of_Planes;
    extern   char Bits_Per_Pixel;
    extern   char Number_Of_Banks;
    extern   char Memory_Model;
    extern   char Bank_Size;
    extern   char Number_Of_ImagePages;
    extern   char Reserved_Page_Function;

    extern   char Red_Mask_Size;
    extern   char Red_Field_Position;
    extern   char Green_Mask_Size;
    extern   char Green_Field_Position;
    extern   char Blue_Mask_Size;
    extern   char Blue_Field_Position;
    extern   char Rsvd_Mask_Size;
    extern   char Rsvd_Field_Position;
    extern   char Direct_Color_Mode_Info;

    extern   void * BasePhys_video;
    extern   void * Off_Screen_Mem_Offset;
    extern   short Off_Screen_Mem_Size;
    extern   char Reserved_vbe20[206];
    #endif
