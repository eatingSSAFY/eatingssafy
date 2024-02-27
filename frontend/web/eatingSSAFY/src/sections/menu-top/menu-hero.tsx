import { useState, useCallback, useEffect } from 'react';
import Image from 'next/image';
import prepPic from 'public/assets/images/cook/chief.png'

import Box from '@mui/material/Box';
import Stack from '@mui/material/Stack';
import Container from '@mui/material/Container';
import Grid from '@mui/material/Grid';
import { Card, Typography } from '@mui/material';

import { HEADER } from 'src/layouts/config-layout';
import MenuHeader from './menu-header';
import MenuCarousel from './details/menu-carousel';
import MenuList from './details/menu-list';
import useComponentSize from 'src/hooks/use-component-size';
import { floors, thisWeekDates, todayIndex } from './menu-constants';
import { myMenus, amounts, preferences } from './menu-interface';

import { getAmount } from 'src/api/request';
import { getPreference } from 'src/api/request';
// import { IconButton } from 'yet-another-react-lightbox';
// ----------------------------------------------------------------------

// menus는 myMenus[] 타입, 즉 myMenus 객체의 배열
export default function MenuHero({ menus }: { menus: myMenus[]; }) {
  const dates: string[] = thisWeekDates;

  // 층 초기 값 10층
  const [dateState, setDateState] = useState(dates[todayIndex]);
  const [floorState, setFloorState] = useState(floors[0]);

  const handleDateChange = (newDate: string) => {
    setDateState(newDate);
  }
  const handleFloorChange = (newFloor: string) => {
    setFloorState(newFloor);
  }

  const selMenuData: myMenus[] = menus.filter((item) => {
    // "2024-01-30화" => "2024-01-30"
    if (dateState.slice(0, -1) === item.servingAt) {
      if (floorState === '10층') {
        if (item.category === '도시락' || item.category === '샌드위치' || item.category === '샐러드') return true
      }
      else {
        if (item.category === '한식' || item.category === '일품') return true
      }
    }
  })

  const [componentRef, size] = useComponentSize();

  const [viewMode, setViewMode] = useState<string>('carousel');

  const handleChangeViewMode = useCallback(
    (event: React.MouseEvent<HTMLElement>, newAlignment: string | null) => {
      if (newAlignment !== null) {
        setViewMode(newAlignment);
      }
    }, []);

  // 재고 변수
  const [amounts, setAmounts] = useState<amounts[]>([]);
  // 따봉 변수
  const [menuPreferences, setMenuPreferences] = useState<preferences[]>([]);

  useEffect(() => {
    setViewMode('list');
    getAmount().then((response: amounts[]) => {
      setAmounts(response);
    getPreference().then((response: preferences[]) => {
      setMenuPreferences(response);
    });
    });
    const timer = setInterval(() => {
      getAmount().then((response: amounts[]) => {
        setAmounts(response);
      });
    }, 3000);
    return () => {
      clearInterval(timer);
    };
  }, []);

  useEffect(()=> {
    getPreference().then((response: preferences[]) => {
      setMenuPreferences(response);
    });
  }, [dateState, floorState]);

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
        <Stack
          sx={{
            textAlign: { md: 'center' },
            height: 1,
          }}
        >
          <MenuHeader
            dates={dates}
            dateState={dateState}
            floorState={floorState}
            viewMode={viewMode}
            handleDateChange={handleDateChange}
            handleFloorChange={handleFloorChange}
            handleChangeViewMode={handleChangeViewMode}
          />
          <Box sx={{ display: 'flex', justifyContent: 'flex-end' }}>
          </Box>
          <Box ref={componentRef} sx={{
            flexGrow: 1,
          }}>
            {selMenuData.length !== 0 &&
              (viewMode === 'carousel' ? 
                (
                  <MenuCarousel
                    carouselData={selMenuData}
                    amountData={amounts}
                    preferenceData={menuPreferences}
                    currentFloor={floorState}
                    currentDate={dateState}
                    isToday={dateState === dates[todayIndex] ? true : false}
                  />
                ) : 
                (
                  <Grid container>
                    {/* <Grid item xs={12}></Grid> */}
                    <MenuList
                      reqHeight={size.height - 20}
                      listData={selMenuData}
                      amountData={amounts}
                      preferenceData={menuPreferences}
                      currentDate={dateState}
                      currentFloor={floorState}
                      isToday={dateState === dates[todayIndex] ? true : false}
                    />
                  </Grid>
                )
              ) || 
              (
                <Card
                  variant='outlined'
                  sx={{
                    mx:1.5,mt:1,
                    height: '83%',
                    boxShadow:'1px 2px 6px 0 rgba(145, 158, 171, 0.4)',
                    backgroundColor:'#00000010',
                    color:'#00000080',
                    display:'flex'
                  }}
                >
                  <Stack spacing={2} sx={{m:'auto',pb:2, fontSize:'1.2rem', fontWeight:'700', }} textAlign='center'>
                    <Image src={prepPic} alt='not' width={200} height={190} style={{opacity:0.6}}></Image>
                    <>404 Menu Not Found! </>
                  </Stack>
                </Card>
              )
            }
          </Box>
        </Stack>
      </Container>
    </Box>
  );
}