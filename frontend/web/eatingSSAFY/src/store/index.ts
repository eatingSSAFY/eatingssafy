import { create } from 'zustand';
import { persist } from 'zustand/middleware';

// ----------------------------------------------------------------
// 유저 정보

interface UserInfo {
  kakaoId: string;
  personNickname: string;
}

interface userState {
  isLogin: boolean;
  pigPower: number,
  userInfo: UserInfo;
  setIsLogin: (loginStatus: boolean) => void;
  setUserInfo: (newUserInfo: UserInfo) => void;
  setPigPower: (pigPower: number) => void;
}

const useUserStore = create(
  persist<userState>(
    (set) => ({
      isLogin: false,
      pigPower: 0,
      userInfo: { kakaoId: '', personNickname: ''},
      setIsLogin: (loginStatus) => set({ isLogin: loginStatus }),
      setUserInfo: (userInfo) => set({ userInfo: userInfo }),
      setPigPower: (newPigPower) => set({ pigPower: newPigPower }),
    }),
    {
      name: 'userStorage',
    },
  ),
);

// -------------------------------------------------------------------
// Footer 페이지 상태

type router = {
  activeButton: string;
  setActiveButton: (buttonHref: string) => void;
};

export const useRouterStore = create(
  persist<router>(
    (set) => ({
        activeButton: '/',
        setActiveButton: (buttonHref) => set({ activeButton: buttonHref }),
      }),
      {name: 'routerStorage'}
    )
  );
export default useUserStore;
