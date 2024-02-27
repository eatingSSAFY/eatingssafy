import Box from '@mui/material/Box';
import Badge from '@mui/material/Badge';
import Stack from '@mui/material/Stack';
import Button from '@mui/material/Button';
import Drawer from '@mui/material/Drawer';
import Container from '@mui/material/Container';
import IconButton from '@mui/material/IconButton';
import { alpha, useTheme } from '@mui/material/styles';

import { paths } from 'src/routes/paths';
import { RouterLink } from 'src/routes/components';

import { useBoolean } from 'src/hooks/use-boolean';
import { useResponsive } from 'src/hooks/use-responsive';
import { useState, useCallback } from 'react';

import { bgGradient } from 'src/theme/css';

import Iconify from 'src/components/iconify';
import { MegaMenuMobile, MegaMenuDesktopHorizontal } from 'src/components/mega-menu';

import { data } from './config-navigation';


// ----------------------------------------------------------------------

export default function Header() {
  const theme = useTheme();

  const mdUp = useResponsive('up', 'md');

  const menuOpen = useBoolean();


  return (
    <Box>
      <Container
        sx={{
          display: 'flex',
          alignItems: 'center',
          position: 'relative',
          height: { xs: 64, md: 72 },
        }}
      >

        <Stack
          spacing={3}
          direction="row"
          alignItems="center"
          flexGrow={1}
          justifyContent="space-between"
        >
          <Button
            component={RouterLink}
            href={paths.eCommerceDev.product}
            fullWidth={!mdUp}
            size="large"
            color="inherit"
          >
            10층
          </Button>
          <Button
            component={RouterLink}
            href={paths.eCommerceDev.product}
            fullWidth={!mdUp}
            size="large"
            color="inherit"
          >
            20층
          </Button>
        </Stack>
      </Container>
    </Box>
  );
}
