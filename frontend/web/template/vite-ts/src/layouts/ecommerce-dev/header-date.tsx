import Box from '@mui/material/Box'
import Stack from '@mui/material/Stack';
import Button from '@mui/material/Button';
import Container from '@mui/material/Container';
import { alpha, useTheme } from '@mui/material/styles';
import { bgGradient } from 'src/theme/css';
import { paths } from 'src/routes/paths';
import Grid from '@mui/material/Unstable_Grid2';
import { useBoolean } from 'src/hooks/use-boolean';
import { useResponsive } from 'src/hooks/use-responsive';
import { useState, useCallback } from 'react';
import { borders } from '@mui/system';

import { useSetRecoilState } from 'recoil';
import { menuListdateFilterState, menuListfloorFilterState } from "../../store";

import Iconify from 'src/components/iconify';
import { MegaMenuMobile, MegaMenuDesktopHorizontal } from 'src/components/mega-menu';

import { data } from './config-navigation';
import { maxWidth } from '@mui/system';
import React from 'react';

// ----------------------------------------------------------------------

// date와 floor 합친 Header
// 날짜 관련 함수 수정 필요



const dates = ['12/15', '12/16', '12/17', '12/18', '12/19']
const floors = ['10층', '20층']


export default function Header() {
  const [clickedDateIndex, setDateClicked] = React.useState(-1);
  const [clickedFloorIndex, setFloorClicked] = React.useState(-1);

  const setdatefilter = useSetRecoilState(menuListdateFilterState);
  const setfloorfilter = useSetRecoilState(menuListfloorFilterState);
  
  const theme = useTheme();
  
  const mdUp = useResponsive('up', 'md');

  const menuOpen = useBoolean();


  return (
    <Box sx={{
      px:2,
      mt:1,
      mb:2,
    }}
    >
      <Grid container spacing={{ xs: 1.5, md: 4 }}>
        <Grid xs={12} md={12} lg={12}>
          <Stack
            spacing={1}
            direction="row"
            alignItems="center"
            flexGrow={1}
            justifyContent="space-evenly"
            sx={{

            }}
          >
            {dates.map((date, index) => (
              <Button key = {date}
                sx={{
                  px: 1,
                  mx: 0,
                  minWidth: 0,
                  borderColor: clickedDateIndex === index ? '#000000': '#00000',
                  "&:hover": {boxShadow:'none'},
                }}
                fullWidth
                size="medium"
                color="inherit"
                variant='outlined'
                onClick={()=>{
                  // console.log(index);
                  setDateClicked(index);
                  setdatefilter(dates[index]);
                }}
              >
                {date}
              </Button>
            ))}
          </Stack>


        </Grid>
        <Grid xs={12} md={12} lg={12}>
          <Stack 
            spacing={2}
            direction="row"
            alignItems="center"
            flexGrow={1}
            justifyContent="space-evenly"
          >
            {floors.map((floor, index) => (
              <Button key = {floor}
              fullWidth
              size="medium"
              color="inherit"
              variant='outlined'
              sx ={{
                borderColor: clickedFloorIndex === index ? '#000000': '#00000',
                "&:hover": {boxShadow:'none'},
              }}
              onClick={()=>{
                // console.log(index)
                setFloorClicked(index);
                setfloorfilter(floor);
              }}
            >
              {floor}
            </Button>
            ))}
          </Stack>
        </Grid>
      </Grid>
    </Box>
  );
}
