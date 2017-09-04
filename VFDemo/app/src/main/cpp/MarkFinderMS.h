//#############################################################################
//
//	���L���O�W���l�����@Shot Note Android�A�v���p�}�[�N�F���摜�������C�u����
//		ver 0.0.01
//-----------------------------------------------------------------------------
//	����
//  [4/11/2011]		ver 0.0.01 �߂��肠���Ή��E�m�[�g�w�b�_����F���Ή�
//
//#############################################################################

#ifndef	__MS_SHOT_NOTE_MARK_FINDER__
#define	__MS_SHOT_NOTE_MARK_FINDER__

//////////////////////////////////////////////////////////////////////////
//	�萔��`
//////////////////////////////////////////////////////////////////////////

typedef int SMF_RESULT;

//	�߂�l
#define	SMF_SUCCESS ( 0 )
#define	SMF_ERROR_BASE		( 0x80000000 )
#define SMF_ERROR_BAD_IMG	( SMF_ERROR_BASE + 0x01 )
#define	SMF_ERROR_BAD_PARAM	( SMF_ERROR_BASE + 0x02 )
#define	SMF_FAILED			( SMF_ERROR_BASE + 0x10 )

//	�摜���k�`��
#define	SMF_BI_RGB	( 0 )

//	�ݒ�
#define SMF_RESIZE			( 0x00000001 )
#define	SMF_LARGEIMAGE_MODE	( 0x00000008 )

//	�ʒu����p�̃}�[�J�[�̈ʒu
typedef enum _tagSMF_SQUARE_POSITION{
	SMF_RIGHT_TOP,
	SMF_LEFT_BOTTOM,
	SMF_RIGHT_BOTTOM,
	SMF_LEFT_TOP
} SMF_SQUARE_POSITION;

//////////////////////////////////////////////////////////////////////////
//	�\���̒�`
//////////////////////////////////////////////////////////////////////////

typedef struct _tagSMF_IMAGE {
	unsigned long	imSize;
	unsigned long	imWidth;
	unsigned long	imHeight;
	unsigned long	imBitCount;
	unsigned long	imCompression;
	unsigned char*	imPixeles;
} SMF_IMAGE;

typedef struct _tagMS_PARAM {
	unsigned long	prSize;
	unsigned long	prMode;
} SMF_PARAM;

typedef struct _tagMS_POSITION {
	unsigned long	x;
	unsigned long	y;
} SMF_POSITION;

typedef struct _tagSMF_MARKER_INFO
{
	unsigned long		miSize;
	SMF_POSITION		miPos[ 4 ];
	SMF_SQUARE_POSITION	miSqPosition;
} SMF_MARKER_INFO;

//////////////////////////////////////////////////////////////////////////
//	�֐���`
//////////////////////////////////////////////////////////////////////////

SMF_RESULT	FindShotNoteMarks( SMF_IMAGE* pImg, SMF_PARAM* pParam, SMF_MARKER_INFO* pMarkInfo );

#endif /* __MS_SHOT_NOTE_MARK_FINDER__ */