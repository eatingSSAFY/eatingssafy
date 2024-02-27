import { useEffect } from 'react';

import { useRecoilValue, useSetRecoilState, useRecoilState } from 'recoil';
import { menuListState } from "../store"


export const DataLoader = () => {
    const setMenuList = useSetRecoilState(menuListState);
    const menuList = useRecoilValue(menuListState);
  
    useEffect(() => {
      fetch('http://localhost:5001/product') // 받아올 서버 URL
        .then((response) => {
          if (!response.ok) {
            throw new Error('Network response was not ok');
          }
          return response.json();
        })
        .then((menuItems) => {
          // console.log(menuItems)
          setMenuList(menuItems); // 불러온 데이터를 Recoil 상태에 저장
          // console.log(menuList)
        })
        .catch((error) => {
          console.error('Could not fetch data:', error);
        });
    }, [setMenuList]); // 의존성 배열에 setMenuList 추가
    // 상태가 업데이트된 후를 감지하기 위한 useEffect
    useEffect(() => {
      // console.log(menuList); // 상태가 업데이트된 후에 실행됩니다.
    }, [menuList]);
  
    return null; // 이 컴포넌트는 UI를 렌더링하지 않음
  }