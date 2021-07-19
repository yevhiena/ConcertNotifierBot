package com.alevel.concertnotifierbot.util;

import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Emoji {
    LINK(EmojiParser.parseToUnicode(":link:")),
    DATE(EmojiParser.parseToUnicode(":date:")),
    MONEY(EmojiParser.parseToUnicode(":money_with_wings:")),
    UNSUBSCRIBE(EmojiParser.parseToUnicode(":x:")),
    MAP(EmojiParser.parseToUnicode(":world_map:")),
    SPEAKER(EmojiParser.parseToUnicode(":speaker:")),
    PIN(EmojiParser.parseToUnicode(":round_pushpin:")),
    PAPERCLIP(EmojiParser.parseToUnicode(":paperclip:")),
    SUBSCRIBE(EmojiParser.parseToUnicode(":heavy_check_mark:")),
    NOTIFICATION_BELL(EmojiParser.parseToUnicode(":bell:")),
    HEADPHONES(EmojiParser.parseToUnicode(":headphones:")),
    QUESTION(EmojiParser.parseToUnicode(":grey_question:"));

    private String emojiName;

    @Override
    public String toString() {
        return emojiName;
    }
}
