import { Box, Card, Container, Grid, Stack, Typography, Divider } from "@mui/material";
import {FormControlLabel, FormGroup, Switch} from '@mui/material';

import { HEADER } from 'src/layouts/config-layout';
import useUserStore from 'src/store';

import { useEffect, useState } from "react";
import {useCookies} from 'react-cookie';
import { notiData, prefData, amountData } from "./settings-interface";

// ----------------------------------------------------------------------


//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////

export default function SettingsHero({initConfig, isMobile, isNotiPermitted}:{initConfig:notiData, isMobile?:boolean, isNotiPermitted:string}) {
  const [cookies, setCookie] = useCookies(['userId']);
  const permission:boolean = isNotiPermitted==="true" ? true:false;

  console.log(cookies.userId)

  const [curAmountConfig, setAmountConfig] = useState<amountData>(
    {amountNoti:initConfig.amountNoti}
  )

  const [curPrefConfig, setPrefConfig] = useState<prefData>(
    {preferenceNoti:initConfig.preferenceNoti}
  )

  function changeAmount(message: string, mes1: string) {
    if (window.AndroidInterface && window.AndroidInterface.changeAmount) {
      // If available, call the changeAmount method with the message
      window.AndroidInterface.changeAmount(message, mes1);
    } else {
      // console.error('AndroidInterface.changeNoti method is not available');
    }
  }
  
  function changePref(message: string, mes1: string) {
    if (window.AndroidInterface && window.AndroidInterface.changePref) {
      // If available, call the changePref method with the message
      window.AndroidInterface.changePref(message, mes1, cookies.userId === undefined?"null":cookies.userId.toString());
    } else {
      // console.error('AndroidInterface.changeNoti method is not available');
    }
  }

  function handleAmountChange(event: React.ChangeEvent<HTMLInputElement>) {
    const newAmount:amountData = {amountNoti: event.target.checked};
    setAmountConfig(newAmount);
  }

  function handlePrefChange(event: React.ChangeEvent<HTMLInputElement>) {
    const newPref:prefData = {preferenceNoti: event.target.checked};
    setPrefConfig(newPref);
  }

  const {
    isLogin,
  } = useUserStore();
  console.log(!permission || !isLogin);

  useEffect(()=>{
    changeAmount(`${curPrefConfig.preferenceNoti}`,`${curAmountConfig.amountNoti}`)
  }, [curAmountConfig])

  useEffect(()=>{
    changePref(`${curPrefConfig.preferenceNoti}`,`${curAmountConfig.amountNoti}`)
  }, [curPrefConfig])
  
  return (
    <Box
      sx={{
        overflow: 'hidden',
        position: 'relative',
        height: {
          md: `calc(100vh - ${HEADER.H_DESKTOP}px)`,
          xs: `calc(100vh - ${HEADER.H_MOBILE}px)`
        },
      }}
    >
      <Container sx={{ height: 1 }}>
        <Stack sx={{ height: 1, }}>
          <Grid container>
            <Grid item xs={12}></Grid>
              <Card
                sx={{
                  mt: 2,
                  mb: 2,
                  pb: 4,
                  mx: 'auto',
                  border: '1px solid rgba(145, 158, 171, 0.55)',
                  height: 'auto',
                  width: 0.97,
                  borderRadius: 1,
                  bgcolor: '#e3eef566',
                  boxShadow: '1px 2px 6px 0 rgba(145, 158, 171, 0.4)',
                }}>
                  <Typography 
                    variant='h4' 
                    sx={{
                      py:1,
                      px:3,
                      backgroundColor:`#e3eef5d9`,
                      opacity:0.8
                  }}>
                  설정
                  </Typography>
                  <Divider sx={{mb:2}}/>
                  
                  <Divider textAlign="left" sx={{mb:2}}><Typography 
                    variant='h6' 
                    sx={{ opacity:0.7, px:2}}>
                  모바일 앱 알림 설정
                  </Typography></Divider>
                  {isMobile?
                    <FormGroup aria-label='position'>
                      <Typography variant='caption' sx={{ opacity:0.6, px:2, mb:0.5, mt:-0.5}}>※알림 권한을 허용해야 사용가능합니다.</Typography>
                      <FormControlLabel control={<Switch disableRipple disabled={!permission} checked={curAmountConfig.amountNoti} onChange={handleAmountChange} color='success' name='amountNoti'/>} label="재고 소진 알림" labelPlacement="start" sx={{justifyContent:'flex-end'}} />
                      <FormControlLabel  control={<Switch disabled={(!permission) || (!isLogin)} disableRipple checked={curPrefConfig.preferenceNoti} onChange={handlePrefChange} color='success' name='preferenceNoti'/>} label="선호 메뉴 알림" labelPlacement="start" sx={{justifyContent:'flex-end'}}/>
                      <Typography variant='caption' sx={{ opacity:0.5, px:2, mt:-0.5}}>&nbsp;&nbsp;※선호 메뉴 알림 설정은 로그인이 필요합니다.</Typography>
                    </FormGroup>:
                    <FormGroup aria-label='position'>
                      <Typography variant='caption' sx={{ opacity:0.6, px:2}}>※알림 설정은 모바일 앱만 가능합니다.</Typography>
                      <FormControlLabel disabled control={<Switch color='success' name='amountNoti'/>} label="재고 소진 알림" labelPlacement="start" sx={{justifyContent:'flex-end'}} />
                      <FormControlLabel disabled control={<Switch color='success' name='preferenceNoti'/>} label="선호 메뉴 알림" labelPlacement="start" sx={{justifyContent:'flex-end'}}/>
                      <Typography variant='caption' sx={{ opacity:0.5, px:2, mt:-0.5}}>&nbsp;&nbsp;※선호 메뉴 알림 설정은 로그인이 필요합니다.</Typography>
                    </FormGroup>
                  }
              </Card>
          </Grid>
        </Stack>
      </Container>
    </Box>


  );
}
