'use client';

import MyPageHero from '../my-page-hero';
import { useState, useEffect } from 'react';
import Loading from 'src/app/loading';
import { useRouter } from 'next/navigation';
import Cookies from 'js-cookie';

// ----------------------------------------------------------------------

export default function MyPageView() {

  const [isLoading, setIsLoading] = useState(true);
  const userCookie = Cookies.get('userId');

  const router = useRouter();

  useEffect(() => {
    if (!userCookie) {
      router.push('/auth/user-login');
      return;
    }
    setIsLoading(false);
  }, [])

  return (
    <>
      {!isLoading ? <MyPageHero /> : <Loading />}
    </>
  );
}