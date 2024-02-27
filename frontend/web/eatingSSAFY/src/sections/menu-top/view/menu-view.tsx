'use client';

import MenuHero from '../menu-hero';
import { useState, useEffect } from 'react';
import { getMenu, getUser, postPigPower, getPigPower } from 'src/api/request';
import type { myMenus } from '../menu-interface';
import Loading from 'src/app/loading';
import useUserStore from 'src/store';
import Cookies from 'js-cookie';

// ----------------------------------------------------------------------'

export default function MenuView() {
  // 변수 타입 지정 (Generic)
  // useRef로 관리하는 값은 값이 변해도 화면이 렌더링되지 않음
  const [menu, setMenu] = useState<myMenus[]>([]);
  // isLoding을 상태로 관리하고, 두 개의 api호출이 완료되면 컴포넌트 렌더링 되도록 함
  const [isLoading, setIsLoading] = useState(true);
  // 돼지력 올리기
  const {
    setIsLogin,
    setUserInfo,
    setPigPower, } = useUserStore();

  useEffect(() => {

    // 쿠키가 없을 때 로컬스토리지에서 불러와서 등록
    let value: string | null = localStorage.getItem('userStorage');
    if (value !== null) {
      let parsedValue: any = JSON.parse(value);
      const kakaoIdForCookie : string = parsedValue.state.userInfo.kakaoId;
      if (kakaoIdForCookie !== "") {
        // 쿠키에 등록
        Cookies.set('userId', kakaoIdForCookie, { expires: 0.25}); // 1 = 1일(24시간)
      }
    }
    // 쿠키 가져오기
    const userCookie = Cookies.get('userId');

    // 쿠키가 있을 때
    if (userCookie !== undefined) {
      // 유저 정보
      getUser().then(response => {
        setIsLogin(true)
        setUserInfo(response)
        postPigPower({ 'cnt': '1' });
        // 새로고침 할 때마다 돼지력+1 / 돼지력 갱신
        getPigPower().then(response => {
          setPigPower(response.cnt)
        })
      });
    }

    // 메뉴 정보
    getMenu().then(response => {
      setMenu(response);
      setIsLoading(false);
    });

  }, []);

  return (
    <>
      {/* menus 배열이 비어있지 않을 때만 MenuHero를 렌더링 아니면 로딩 페이지*/}
      {!isLoading ? <MenuHero menus={menu} /> : <Loading />}
    </>
  );
}