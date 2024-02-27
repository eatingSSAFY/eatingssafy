// --------------------------------------------------------------------------------------
// 층 , 메뉴 이름 변경 함수

export const floors: string[] = ['10층', '20층'];

// -------------------------------------------------------------------------------------
// 날짜

export let now = new Date();
let day = now.getDay();
// 월요일부터 얼마나 떨어져 있는지
let diffToMonday = now.getDate() - day + (day == 0 ? -6:1);

if (day == 6 || day == 0) {
    diffToMonday += 7;
}

// 이번 주 월 ~ 금 날짜 데이터 "2024-01-30화"
// 날짜 포맷
export let thisWeekDates : string[] = [];

export let monthIsOver10 : number = (new Date().getMonth() + 1) >= 10 ? 1 : 0;
export let dateIsOver10 : number = (new Date().getDate()) >= 10 ? 1 : 0;

// 주말이면 월요일이 기본 값
// 오늘 날짜의 인덱스 => 오늘 날짜가 기본 값이 되도록 함.
// export let todayIndex : number = 0;
export let todayIndex = (day == 6 || day == 0) ? 0 : day - 1;

if(day >= 1 && day <= 5) { // 월요일부터 금요일까지
    todayIndex = day - 1; // 월요일을 0으로 만들기 위해 -1
}


for(let i = 0; i < 5; i++) {
    let newDate = new Date();
    newDate.setDate(diffToMonday + i);
    let year = newDate.getFullYear();
    let month = ("0" + (newDate.getMonth()+1)).slice(-2);
    let day = ("0" + newDate.getDate()).slice(-2);
    let dayName;
    switch(i){
        case 0: dayName = "월"; break;
        case 1: dayName = "화"; break;
        case 2: dayName = "수"; break;
        case 3: dayName = "목"; break;
        case 4: dayName = "금"; break;
    }
    thisWeekDates.push(`${year}-${month}-${day}${dayName}`);
}

// -------------------------------------------------------------------------
// 재고

export const max_count = 240;