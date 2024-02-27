import axios from 'axios';
import { backendAPI } from './backendAPI';

// 입력 폼 POST
export const addNoCardPerson = async (personData: object) => { // personData = {'personName','personId'}
    await axios.post(backendAPI.POST_NOCARD_PERSON, personData);
};

// [시각화] -------------------------------------
// 상세 메뉴 GET
export const getMenu = async () => {
    const response = await axios.get(backendAPI.GET_MENU);
    return response.data
}


// 10층 메뉴 재고 GET
export const getAmount = async () => {
    const response = await axios.get(backendAPI.GET_AMOUNT);
    return response.data
}

// [층별 식사 시간] ------------------------------
export const getLunchTime = async () => {
    const response = await axios.get(backendAPI.GET_LUNCH_TIME);
    return response.data
}


// [회원] ---------------------------------------
export const getUser = async () => {
    const response = await axios.get(backendAPI.GET_USER);
    return response.data
};


// [따봉] ---------------------------------------
export const addUserPreference = async (data : object) => {
    await axios.post(backendAPI.PREFERENCE, data);
}

export const getPreference = async () => {
    const response = await axios.get(backendAPI.PREFERENCE);
    return response.data
}

export const getUserPreference = async (data: object) => {
    const response = await axios.get(backendAPI.USER_PREFERENCE, data);
    return response.data
}


// [돼지력] -------------------------------------
export const postPigPower = async (data : object) => {
    await axios.post(backendAPI.USER_PIG_POWER, data);    
}

export const getPigPower = async () => {
    const response = await axios.get(backendAPI.USER_PIG_POWER);
    return response.data
    
}

// [알림설정] -------------------------------------
export const getNotiConfig = async (appToken:string) => {
    const response = await axios.get(backendAPI.NOTI_CONFIG, {params: {appToken: appToken}});
    return response.data
}