
import * as React from 'react';

import Box from '@mui/material/Box';
import IconButton from '@mui/material/IconButton';
import { FavoriteBorderRounded, FavoriteRounded } from '@mui/icons-material';

export default function PaperHeader() {

// data input------------------------------------------------------------

  const [checked, setChecked] = React.useState(false);

// ----------------------------------------------------------------------


// favorite check update-------------------------------------------------
  

// ----------------------------------------------------------------------


  return (
    <Box sx={{height:'40px'}}>
      <IconButton color='error' aria-label="add to favorites" onClick={()=>{setChecked(!checked)}}>
        {checked ? <FavoriteRounded/> : <FavoriteBorderRounded/>}
      </IconButton>
    </Box>
  );
}






