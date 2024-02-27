
import * as React from 'react';

import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import LinearProgress, { linearProgressClasses } from '@mui/material/LinearProgress';

import { amounts } from '../menu-interface';


//{cnt} : cntProps
export default function CurrentInventoryBar({ amountData, isCarousel }: { amountData: amounts | undefined, isCarousel : boolean }) {
  const LINE_HEIGHT = 30;
  const LINE_HEIGHT_SMALL = 20;

  // data input------------------------------------------------------------

  // ----------------------------------------------------------------------


  // progressbar interval update-------------------------------------------

  // ----------------------------------------------------------------------
  const curPercent = amountData === undefined ? 0: (Math.floor((amountData.value / amountData.stock) * 100));

  return (
    <Box sx={{ width: isCarousel ? 0.9 : 0.92, mx:'auto'  }}>
      <Box sx={{ 
        pt: 1, 
        mx: 'auto', 
        width: 1,
        position: 'relative',
        textAlign:'initial' }}>
        <Typography
          variant='button'
          textAlign='center'
          sx={{
            position: 'absolute',
            zIndex: 7,
            width: 'inherit',
            height: isCarousel ? LINE_HEIGHT : LINE_HEIGHT_SMALL,
            lineHeight: isCarousel? `${LINE_HEIGHT + 16}px` :  `${LINE_HEIGHT_SMALL + 14}px`,
            ...(!isCarousel && {fontSize:'0.8rem'})
          }}
        >
          {amountData === undefined ? `Loading` : amountData.value !== 0 ? `남은 개수 : ${amountData.value}` : "품절!!!"}
        </Typography>
        <LinearProgress
          variant='determinate'
          value={curPercent}
          color={curPercent>60 ? 'info' : (curPercent > 30?'warning':'error')}
          sx={{
            height: isCarousel ? LINE_HEIGHT : LINE_HEIGHT_SMALL,
            border: isCarousel ? '8px solid #d9d9d900' : '6px solid #d9d9d900',
            borderRadius: 3,
            [`& .${linearProgressClasses.bar}`]: { borderRadius: 2 },
            mb: 2,
            boxSizing: 'content-box',
            boxShadow: '1px 1px 5px grey'
          }}
        >
        </LinearProgress>
      </Box>
    </Box>
  );
}
