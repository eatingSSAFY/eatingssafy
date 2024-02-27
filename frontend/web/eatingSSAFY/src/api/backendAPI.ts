export const backendAPI = {

    POST_NOCARD_PERSON : `/api/nocard-person`,
    GET_NOCARD_PERSON : `/api/nocard-person`,
    GET_MENU : `/api/menu`,
    GET_AMOUNT : `/api/amount`,
    KAKAO_LOGIN : `/oauth2/authorization/kakao`,
    POST_USER_JOIN : `/api/join`,
    GET_USER : `/api/user`,
    GET_LUNCH_TIME : `/api/lunchtime`,
    // 메뉴 좋아요 수 + 유저의 해당 메뉴 좋아요싫어요 // POST 좋아요싫어요
    PREFERENCE : `/api/preference`,
    // 유저 좋아요 리스트
    USER_PREFERENCE : `/api/preference/list`,
    USER_PIG_POWER : `/api/daily-visit`,
    // 알림설정 초기값 조회
    NOTI_CONFIG : `/api/notification/config`,
    
}