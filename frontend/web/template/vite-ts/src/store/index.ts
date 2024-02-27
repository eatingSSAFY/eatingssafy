import { atom, selector } from "recoil";
// import axios from "axios";

// atom

export interface menuItem {
    id: string;
    날짜: string;
    메뉴분류명: string;
    메뉴배급장소: string;
    메뉴상세내용: string;
    잔여량?: number;
}

export const menuListState = atom<menuItem[]>({
    key: "menuListState", // 전역적으로 유일해야 한다.
    default: [],
});

export const menuListdateFilterState = atom({
    key: "menuListdateFilterState",
    // 오늘 날짜로 지정해야 함
    default: "12/19",
});
export const menuListfloorFilterState = atom({
    key: "menuListfloorFilterState",
    default: "10층",
});

export const filteredMenuListState = selector({
    key: 'filteredMenuListState',
    get: ({ get }) => {
      const filterDate = get(menuListdateFilterState);
      const filterFloor = get(menuListfloorFilterState);
      const list = get(menuListState);
  
      // 날짜 필터 적용
      const dateFilteredList = list.filter((item) => item.날짜 === filterDate);
  
      // 층 필터 적용
      const floorFilteredList = dateFilteredList.filter((item) => item.메뉴배급장소 === filterFloor);
  
      // 필터링된 결과 반환
      return floorFilteredList;
    },
  });