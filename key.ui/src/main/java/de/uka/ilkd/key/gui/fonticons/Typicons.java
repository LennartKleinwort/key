package de.uka.ilkd.key.gui.fonticons;

import java.awt.*;
import java.io.IOException;

/*
 * Copyright (c) 2016 jIconFont <BR> <BR> Permission is hereby granted, free of charge, to any
 * person obtaining a copy of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:<BR> <BR>
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.<BR> <BR> THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY
 * OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
public enum Typicons implements IconFont {

    ADJUST_BRIGHTNESS('\uE000'), ADJUST_CONTRAST('\uE001'), ANCHOR('\uE003'),
    ANCHOR_OUTLINE('\uE002'), ARCHIVE('\uE004'), ARROW_BACK('\uE006'), ARROW_BACK_OUTLINE('\uE005'),
    ARROW_DOWN('\uE009'), ARROW_DOWN_OUTLINE('\uE007'), ARROW_DOWN_THICK('\uE008'),
    ARROW_FORWARD('\uE00B'), ARROW_FORWARD_OUTLINE('\uE00A'), ARROW_LEFT('\uE00E'),
    ARROW_LEFT_OUTLINE('\uE00C'), ARROW_LEFT_THICK('\uE00D'), ARROW_LOOP('\uE010'),
    ARROW_LOOP_OUTLINE('\uE00F'), ARROW_MAXIMISE('\uE012'), ARROW_MAXIMISE_OUTLINE('\uE011'),
    ARROW_MINIMISE('\uE014'), ARROW_MINIMISE_OUTLINE('\uE013'), ARROW_MOVE('\uE016'),
    ARROW_MOVE_OUTLINE('\uE015'), ARROW_REPEAT('\uE018'), ARROW_REPEAT_OUTLINE('\uE017'),
    ARROW_RIGHT('\uE01B'), ARROW_RIGHT_OUTLINE('\uE019'), ARROW_RIGHT_THICK('\uE01A'),
    ARROW_SHUFFLE('\uE01C'), ARROW_SORTED_DOWN('\uE01D'), ARROW_SORTED_UP('\uE01E'),
    ARROW_SYNC('\uE020'), ARROW_SYNC_OUTLINE('\uE01F'), ARROW_UNSORTED('\uE021'),
    ARROW_UP('\uE024'), ARROW_UP_OUTLINE('\uE022'), ARROW_UP_THICK('\uE023'), AT('\uE025'),
    ATTACHMENT('\uE027'), ATTACHMENT_OUTLINE('\uE026'), BACKSPACE('\uE029'),
    BACKSPACE_OUTLINE('\uE028'), BATTERY_CHARGE('\uE02A'), BATTERY_FULL('\uE02B'),
    BATTERY_HIGH('\uE02C'), BATTERY_LOW('\uE02D'), BATTERY_MID('\uE02E'), BEAKER('\uE02F'),
    BEER('\uE030'), BELL('\uE031'), BOOK('\uE032'), BOOKMARK('\uE033'), BRIEFCASE('\uE034'),
    BRUSH('\uE035'), BUSINESS_CARD('\uE036'), CALCULATOR('\uE037'), CALENDAR('\uE039'),
    CALENDAR_OUTLINE('\uE038'), CAMERA('\uE03B'), CAMERA_OUTLINE('\uE03A'), CANCEL('\uE03D'),
    CANCEL_OUTLINE('\uE03C'), CHART_AREA('\uE03F'), CHART_AREA_OUTLINE('\uE03E'),
    CHART_BAR('\uE041'), CHART_BAR_OUTLINE('\uE040'), CHART_LINE('\uE043'),
    CHART_LINE_OUTLINE('\uE042'), CHART_PIE('\uE045'), CHART_PIE_OUTLINE('\uE044'),
    CHEVRON_LEFT('\uE047'), CHEVRON_LEFT_OUTLINE('\uE046'), CHEVRON_RIGHT('\uE049'),
    CHEVRON_RIGHT_OUTLINE('\uE048'), CLIPBOARD('\uE04A'), CLOUD_STORAGE('\uE04B'),
    CLOUD_STORAGE_OUTLINE('\uE054'), CODE('\uE04D'), CODE_OUTLINE('\uE04C'), COFFEE('\uE04E'),
    COG('\uE050'), COG_OUTLINE('\uE04F'), COMPASS('\uE051'), CONTACTS('\uE052'),
    CREDIT_CARD('\uE053'), CSS3('\uE055'), DATABASE('\uE056'), DELETE('\uE058'),
    DELETE_OUTLINE('\uE057'), DEVICE_DESKTOP('\uE059'), DEVICE_LAPTOP('\uE05A'),
    DEVICE_PHONE('\uE05B'), DEVICE_TABLET('\uE05C'), DIRECTIONS('\uE05D'), DIVIDE('\uE05F'),
    DIVIDE_OUTLINE('\uE05E'), DOCUMENT('\uE063'), DOCUMENT_ADD('\uE060'), DOCUMENT_DELETE('\uE061'),
    DOCUMENT_TEXT('\uE062'), DOWNLOAD('\uE065'), DOWNLOAD_OUTLINE('\uE064'), DROPBOX('\uE066'),
    EDIT('\uE067'), EJECT('\uE069'), EJECT_OUTLINE('\uE068'), EQUALS('\uE06B'),
    EQUALS_OUTLINE('\uE06A'), EXPORT('\uE06D'), EXPORT_OUTLINE('\uE06C'), EYE('\uE06F'),
    EYE_OUTLINE('\uE06E'), FEATHER('\uE070'), FILM('\uE071'), FILTER('\uE072'), FLAG('\uE074'),
    FLAG_OUTLINE('\uE073'), FLASH('\uE076'), FLASH_OUTLINE('\uE075'), FLOW_CHILDREN('\uE077'),
    FLOW_MERGE('\uE078'), FLOW_PARALLEL('\uE079'), FLOW_SWITCH('\uE07A'), FOLDER('\uE07E'),
    FOLDER_ADD('\uE07B'), FOLDER_DELETE('\uE07C'), FOLDER_OPEN('\uE07D'), GIFT('\uE07F'),
    GLOBE('\uE081'), GLOBE_OUTLINE('\uE080'), GROUP('\uE083'), GROUP_OUTLINE('\uE082'),
    HEADPHONES('\uE084'), HEART('\uE088'), HEART_FULL_OUTLINE('\uE085'),
    HEART_HALF_OUTLINE('\uE086'), HEART_OUTLINE('\uE087'), HOME('\uE08A'), HOME_OUTLINE('\uE089'),
    HTML5('\uE08B'), IMAGE('\uE08D'), IMAGE_OUTLINE('\uE08C'), INFINITY('\uE08F'),
    INFINITY_OUTLINE('\uE08E'), INFO('\uE093'), INFO_LARGE('\uE091'), INFO_LARGE_OUTLINE('\uE090'),
    INFO_OUTLINE('\uE092'), INPUT_CHECKED('\uE095'), INPUT_CHECKED_OUTLINE('\uE094'), KEY('\uE097'),
    KEYBOARD('\uE098'), KEY_OUTLINE('\uE096'), LEAF('\uE099'), LIGHTBULB('\uE09A'), LINK('\uE09C'),
    LINK_OUTLINE('\uE09B'), LOCATION('\uE0A0'), LOCATION_ARROW('\uE09E'),
    LOCATION_ARROW_OUTLINE('\uE09D'), LOCATION_OUTLINE('\uE09F'), LOCK_CLOSED('\uE0A2'),
    LOCK_CLOSED_OUTLINE('\uE0A1'), LOCK_OPEN('\uE0A4'), LOCK_OPEN_OUTLINE('\uE0A3'), MAIL('\uE0A5'),
    MAP('\uE0A6'), MEDIA_EJECT('\uE0A8'), MEDIA_EJECT_OUTLINE('\uE0A7'),
    MEDIA_FAST_FORWARD('\uE0AA'), MEDIA_FAST_FORWARD_OUTLINE('\uE0A9'), MEDIA_PAUSE('\uE0AC'),
    MEDIA_PAUSE_OUTLINE('\uE0AB'), MEDIA_PLAY('\uE0B0'), MEDIA_PLAY_OUTLINE('\uE0AD'),
    MEDIA_PLAY_REVERSE('\uE0AF'), MEDIA_PLAY_REVERSE_OUTLINE('\uE0AE'), MEDIA_RECORD('\uE0B2'),
    MEDIA_RECORD_OUTLINE('\uE0B1'), MEDIA_REWIND('\uE0B4'), MEDIA_REWIND_OUTLINE('\uE0B3'),
    MEDIA_STOP('\uE0B6'), MEDIA_STOP_OUTLINE('\uE0B5'), MESSAGE('\uE0B8'), MESSAGES('\uE0B9'),
    MESSAGE_TYPING('\uE0B7'), MICROPHONE('\uE0BB'), MICROPHONE_OUTLINE('\uE0BA'), MINUS('\uE0BD'),
    MINUS_OUTLINE('\uE0BC'), MORTAR_BOARD('\uE0BE'), NEWS('\uE0BF'), NOTES('\uE0C1'),
    NOTES_OUTLINE('\uE0C0'), PEN('\uE0C2'), PENCIL('\uE0C3'), PHONE('\uE0C5'),
    PHONE_OUTLINE('\uE0C4'), PI('\uE0C7'), PIN('\uE0C9'), PIN_OUTLINE('\uE0C8'), PIPETTE('\uE0CA'),
    PI_OUTLINE('\uE0C6'), PLANE('\uE0CC'), PLANE_OUTLINE('\uE0CB'), PLUG('\uE0CD'), PLUS('\uE0CF'),
    PLUS_OUTLINE('\uE0CE'), POINT_OF_INTEREST('\uE0D1'), POINT_OF_INTEREST_OUTLINE('\uE0D0'),
    POWER('\uE0D3'), POWER_OUTLINE('\uE0D2'), PRINTER('\uE0D4'), PUZZLE('\uE0D6'),
    PUZZLE_OUTLINE('\uE0D5'), RADAR('\uE0D8'), RADAR_OUTLINE('\uE0D7'), REFRESH('\uE0DA'),
    REFRESH_OUTLINE('\uE0D9'), RSS('\uE0DC'), RSS_OUTLINE('\uE0DB'), SCISSORS('\uE0DE'),
    SCISSORS_OUTLINE('\uE0DD'), SHOPPING_BAG('\uE0DF'), SHOPPING_CART('\uE0E0'),
    SOCIAL_AT_CIRCULAR('\uE0E1'), SOCIAL_DRIBBBLE('\uE0E3'), SOCIAL_DRIBBBLE_CIRCULAR('\uE0E2'),
    SOCIAL_FACEBOOK('\uE0E5'), SOCIAL_FACEBOOK_CIRCULAR('\uE0E4'), SOCIAL_FLICKR('\uE0E7'),
    SOCIAL_FLICKR_CIRCULAR('\uE0E6'), SOCIAL_GITHUB('\uE0E9'), SOCIAL_GITHUB_CIRCULAR('\uE0E8'),
    SOCIAL_GOOGLE_PLUS('\uE0EB'), SOCIAL_GOOGLE_PLUS_CIRCULAR('\uE0EA'), SOCIAL_INSTAGRAM('\uE0ED'),
    SOCIAL_INSTAGRAM_CIRCULAR('\uE0EC'), SOCIAL_LAST_FM('\uE0EF'),
    SOCIAL_LAST_FM_CIRCULAR('\uE0EE'), SOCIAL_LINKEDIN('\uE0F1'),
    SOCIAL_LINKEDIN_CIRCULAR('\uE0F0'), SOCIAL_PINTEREST('\uE0F3'),
    SOCIAL_PINTEREST_CIRCULAR('\uE0F2'), SOCIAL_SKYPE('\uE0F5'), SOCIAL_SKYPE_OUTLINE('\uE0F4'),
    SOCIAL_TUMBLER('\uE0F7'), SOCIAL_TUMBLER_CIRCULAR('\uE0F6'), SOCIAL_TWITTER('\uE0F9'),
    SOCIAL_TWITTER_CIRCULAR('\uE0F8'), SOCIAL_VIMEO('\uE0FB'), SOCIAL_VIMEO_CIRCULAR('\uE0FA'),
    SOCIAL_YOUTUBE('\uE0FD'), SOCIAL_YOUTUBE_CIRCULAR('\uE0FC'), SORT_ALPHABETICALLY('\uE0FF'),
    SORT_ALPHABETICALLY_OUTLINE('\uE0FE'), SORT_NUMERICALLY('\uE101'),
    SORT_NUMERICALLY_OUTLINE('\uE100'), SPANNER('\uE103'), SPANNER_OUTLINE('\uE102'),
    SPIRAL('\uE104'), STAR('\uE109'), STARBURST('\uE10B'), STARBURST_OUTLINE('\uE10A'),
    STAR_FULL_OUTLINE('\uE105'), STAR_HALF('\uE107'), STAR_HALF_OUTLINE('\uE106'),
    STAR_OUTLINE('\uE108'), STOPWATCH('\uE10C'), SUPPORT('\uE10D'), TABS_OUTLINE('\uE10E'),
    TAG('\uE10F'), TAGS('\uE110'), THERMOMETER('\uE119'), THUMBS_DOWN('\uE11A'),
    THUMBS_OK('\uE11B'), THUMBS_UP('\uE11C'), TH_LARGE('\uE112'), TH_LARGE_OUTLINE('\uE111'),
    TH_LIST('\uE114'), TH_LIST_OUTLINE('\uE113'), TH_MENU('\uE116'), TH_MENU_OUTLINE('\uE115'),
    TH_SMALL('\uE118'), TH_SMALL_OUTLINE('\uE117'), TICK('\uE11E'), TICKET('\uE11F'),
    TICK_OUTLINE('\uE11D'), TIME('\uE120'), TIMES('\uE122'), TIMES_OUTLINE('\uE121'),
    TRASH('\uE123'), TREE('\uE124'), UPLOAD('\uE126'), UPLOAD_OUTLINE('\uE125'), USER('\uE12C'),
    USER_ADD('\uE128'), USER_ADD_OUTLINE('\uE127'), USER_DELETE('\uE12A'),
    USER_DELETE_OUTLINE('\uE129'), USER_OUTLINE('\uE12B'), VENDOR_ANDROID('\uE12D'),
    VENDOR_APPLE('\uE12E'), VENDOR_MICROSOFT('\uE12F'), VIDEO('\uE131'), VIDEO_OUTLINE('\uE130'),
    VOLUME('\uE135'), VOLUME_DOWN('\uE132'), VOLUME_MUTE('\uE133'), VOLUME_UP('\uE134'),
    WARNING('\uE137'), WARNING_OUTLINE('\uE136'), WATCH('\uE138'), WAVES('\uE13A'),
    WAVES_OUTLINE('\uE139'), WEATHER_CLOUDY('\uE13B'), WEATHER_DOWNPOUR('\uE13C'),
    WEATHER_NIGHT('\uE13D'), WEATHER_PARTLY_SUNNY('\uE13E'), WEATHER_SHOWER('\uE13F'),
    WEATHER_SNOW('\uE140'), WEATHER_STORMY('\uE141'), WEATHER_SUNNY('\uE142'),
    WEATHER_WINDY('\uE144'), WEATHER_WINDY_CLOUDY('\uE143'), WINE('\uE147'), WI_FI('\uE146'),
    WI_FI_OUTLINE('\uE145'), WORLD('\uE149'), WORLD_OUTLINE('\uE148'), ZOOM('\uE14F'),
    ZOOM_IN('\uE14B'), ZOOM_IN_OUTLINE('\uE14A'), ZOOM_OUT('\uE14D'), ZOOM_OUTLINE('\uE14E'),
    ZOOM_OUT_OUTLINE('\uE14C');

    private static Font font;
    private final char character;

    Typicons(char character) {
        this.character = character;
    }

    @Override
    public Font getFont() throws IOException, FontFormatException {
        if (font == null) {
            font = Font.createFont(Font.TRUETYPE_FONT,
                getClass().getResourceAsStream("/fonts/typicons.ttf"));
        }
        return font;

    }

    @Override
    public char getUnicode() {
        return character;
    }
}