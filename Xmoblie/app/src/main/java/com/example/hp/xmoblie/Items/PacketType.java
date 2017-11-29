package com.example.hp.xmoblie.Items;

/**
 * Created by HP on 2017-11-28.
 */

public enum PacketType {
    PT_None,
    PT_ValidToken,
    PT_ValidTokenReply,
    PT_InitDownload,
    PT_InitDownloadResult,
    PT_RequestDownload,
    PT_FileDownload,
    PT_IsDir,
    PT_lsDirReply,
    PT_UploadFile,
    PT_UploadReply,
    PT_UploadContinue,
    PT_CreateFile,
    PT_CreateFileReply,
    PT_UploadDone,
    PT_UploadDoneReply;

}
