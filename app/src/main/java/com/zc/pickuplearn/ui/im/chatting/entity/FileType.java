package com.zc.pickuplearn.ui.im.chatting.entity;


public enum FileType {

    image,
    audio,
    video,
    document,
    other;


    public static FileType getFileTypeByOrdinal(int ordinal) {

        for (FileType type : values()) {

            if (type.ordinal() == ordinal) {

                return type;
            }

        }

        return image;

    }
}