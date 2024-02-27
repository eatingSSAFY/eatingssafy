'use client';

import { useEffect, useState } from 'react';
import SettingsHero from '../settings-hero';
import Loading from 'src/app/loading';
import { notiData } from '../settings-interface';
import { getNotiConfig } from 'src/api/request';
import { useRef } from 'react';

// ----------------------------------------------------------------------'

declare global {
  interface Window {
    AndroidInterface?: {
      sendDataToWeb: () => void;
      sendPermReqWeb: () => void;
      changeAmount: (msg: string, msg2: string) => void;
      changePref: (msg: string, msg2: string, userId: string) => void;
    };
    receiveDataFromApp: (token: string) => void;
    receivePermFromApp: (permission : boolean) => void;
  }
}

// 요청 보냈다 받기

export default function SettingsView() {
  const [isMobile, setMobile] = useState<boolean|undefined>(
    typeof window !== 'undefined' ? (window.AndroidInterface?true:false):undefined
  );

  const [isLoading, setIsLoading] = useState(true);
  const [isNotiPermitted, setIsNotiPermitted] = useState<string>("false");

  const initConfig = useRef<notiData>(
    {
      preferenceNoti: false,
      amountNoti: false
    }
  );

  typeof window !== "undefined" ?
  window.receiveDataFromApp = function (token: string) {
    getNotiConfig(token).then((response: notiData) => {
      initConfig.current = response;
      setIsLoading(false);
    })
  }:null;

  typeof window !== "undefined" ?
  window.receivePermFromApp = function (permission: boolean) {
    setIsNotiPermitted(permission.toString())
  }:null;

  function sendDataToWeb() {
    if (window.AndroidInterface && window.AndroidInterface.sendDataToWeb) {
      // If available, call the sendDataToWeb method with the message
      window.AndroidInterface.sendDataToWeb();
    } else {
      // console.error('AndroidInterface.sendDataToWeb method is not available');
    }
  }

  function sendPermReqWeb() {
    if (window.AndroidInterface && window.AndroidInterface.sendPermReqWeb) {
      // If available, call the sendPermReqWeb method with the message
      window.AndroidInterface.sendPermReqWeb();
    } else {
      // console.error('AndroidInterface.sendPermReqWeb method is not available');
    }
  }

  useEffect(() => {
    if (isMobile) {
      sendDataToWeb();
      sendPermReqWeb();
    }
    else{
      setIsLoading(false);
    }
  }, [])


  return (
    <>
      {!isLoading ? <SettingsHero initConfig={initConfig.current} isMobile={isMobile} isNotiPermitted={isNotiPermitted} /> : <Loading />}
    </>
  );
}