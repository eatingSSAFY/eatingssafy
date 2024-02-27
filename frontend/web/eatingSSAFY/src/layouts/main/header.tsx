import Image from 'next/image'
import * as React from 'react';

import Box from '@mui/material/Box';
import Link from '@mui/material/Link';
import Stack from '@mui/material/Stack';
import IconButton from '@mui/material/IconButton';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Container from '@mui/material/Container';
import { useTheme } from '@mui/material/styles';
import piggyPic from 'public/assets/piggy.png';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';

// import { useOffSetTop } from 'src/hooks/use-off-set-top';
// import { useResponsive } from 'src/hooks/use-responsive';

import { bgBlur } from 'src/theme/css';

import Logo from 'src/components/logo';

// import NavMobile from './nav/mobile';
// import NavDesktop from './nav/desktop';
import { HEADER } from '../config-layout';
// import HeaderShadow from '../common/header-shadow';
// import { primary } from 'src/theme/palette';
import Iconify from 'src/components/iconify/iconify';
import useUserStore from 'src/store';
import { useRouter } from 'next/navigation';

// ----------------------------------------------------------------------

type Props = {
  headerOnDark: boolean;
};

export default function Header({ headerOnDark }: Props) {
  const theme = useTheme();

  const router = useRouter();

  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const open = Boolean(anchorEl);
  const handleClick = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };

  // const offset = useOffSetTop();

  // const mdUp = useResponsive('up', 'md');

  const { pigPower, isLogin, userInfo } = useUserStore();



  const renderContent = (
    <>
      <Box onClick={() => {router.push('/')}} sx={{ lineHeight: 0, position: 'relative' }}>
        <Logo />
      </Box>
      <>
      {isLogin &&
        <Stack
          flexGrow={1}
          alignItems="center"
          sx={{
            height: HEADER.H_MOBILE,
            lineHeight: `${HEADER.H_MOBILE}px`,
            fontWeight: 'bold',
            fontSize: '0.9rem',
          }}
        >
          Hi ~ {userInfo.personNickname}님
        </Stack>}

        <Box sx={{ flexGrow: { xs: 1, md: 'unset' } }} />
      </>
      {isLogin &&
        <Stack spacing={1} direction="row" alignItems="center" justifyContent="center">
          <Image src={piggyPic} alt={'돼지력'} width={24} height={24} ></Image>
          <>{pigPower}</>
          <IconButton disableRipple onClick={handleClick} size='small' sx={{ p: 0, width: '22px' }}>
            <Iconify icon='uiw:question-circle-o'></Iconify>
          </IconButton>
          <Menu
            anchorEl={anchorEl}
            id="account-menu"
            open={open}
            onClose={handleClose}
            onClick={handleClose}
            PaperProps={{
              elevation: 0,
              sx: {
                overflow: 'visible',
                filter: 'drop-shadow(0px 2px 8px rgba(0,0,0,0.32))',
                mt: 1.5,
                '&::before': {
                  content: '""',
                  display: 'block',
                  position: 'absolute',
                  top: 0,
                  right: 14,
                  width: 10,
                  height: 10,
                  bgcolor: 'background.paper',
                  transform: 'translateY(-50%) rotate(45deg)',
                  zIndex: 0,
                },
              },
            }}
            transformOrigin={{ horizontal: 'right', vertical: 'top' }}
            anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
          >
            <MenuItem onClick={handleClose}>
              돼지력 : 오늘 메뉴를 본 횟수!
            </MenuItem>
            <Box sx={{fontSize:'0.7rem', opacity:0.7, p:1}}>
              Icon made by justicon<br/>from www.flaticon.com
            </Box>
          </Menu>
        </Stack>
      }
    </>
  );

  return (
    <AppBar position='relative' sx={{ height: HEADER.H_MOBILE }}>
      <Toolbar
        variant='dense'
        disableGutters
        sx={{
          ...bgBlur({ color: '#FFFFFF', opacity: 0.35 }),
          boxShadow: '0 1px 3px 0 #d8d8d8',
          "& .MuiToolbar-root": { minHeight: HEADER.H_MOBILE }
        }}
      >
        <Container
          sx={{
            height: 1,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
          }}
        >
          {renderContent}
        </Container>
      </Toolbar>

      {/* {offset && <HeaderShadow />} */}
    </AppBar>
  );
}
