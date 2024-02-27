import * as React from 'react';
import CircularProgress, {
  CircularProgressProps,
} from '@mui/material/CircularProgress';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import { amounts } from '../menu-interface';

interface CircularProgressNewProps extends CircularProgressProps {
  curstock : number;
}

function CircularProgressWithLabel(
  props: CircularProgressNewProps & { value: number }) {
    // console.log(`value_${props.value}`)
    // console.log(`curStock_${props.curstock}`)
  return (
    <Box sx={{ position: 'relative', display: 'inline-flex', mx:'auto', mb:1.5 }}>
      <CircularProgress size={50} thickness={6} variant="determinate" {...props}
        color={props.value > 60 ? 'info' : (props.value >30 ? 'warning' : 'error')}
      />
      <Box
        sx={{
          top: 0,
          left: 0,
          bottom: 0,
          right: 0,
          position: 'absolute',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
        }}
      >
        <Typography
          variant="caption"
          component="div"
          color="text.secondary"
          fontWeight={'bold'}
        >{`${props.curstock}`}</Typography>
      </Box>
    </Box>
  );
}

export default function CurrentInventoryCircular({amountData}:{amountData:amounts|undefined}) {
  const curPercent = amountData === undefined ? 0 : Math.floor((amountData.value / amountData.stock) * 100)
  return <CircularProgressWithLabel value={curPercent} curstock={amountData === undefined ? 0 : amountData.value}/>;
}