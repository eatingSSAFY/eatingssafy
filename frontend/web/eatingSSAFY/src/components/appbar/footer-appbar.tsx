import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import AccountCircleOutlinedIcon from '@mui/icons-material/AccountCircleOutlined';
import RestaurantMenuOutlinedIcon from '@mui/icons-material/RestaurantMenuOutlined';
import SettingsIcon from '@mui/icons-material/Settings';
import CampaignIcon from '@mui/icons-material/Campaign';
import CampaignOutlinedIcon from '@mui/icons-material/CampaignOutlined';
import { bgBlur } from 'src/theme/css';
import Link from '@mui/material/Link';
import { RouterLink } from 'src/routes/components';
import useUserStore from 'src/store';
import { usePathname } from 'next/navigation';


export default function FooterAppBar() {
  const pathname = usePathname();
   
  const {
    isLogin,
  } = useUserStore();

  return (
    <AppBar position="fixed" sx={{
      display: 'flex',
      top: 'auto',
      bottom: 0,
      left: 0,
      maxWidth: '600px',
      width: '100%',
    }}>
      <Toolbar variant='dense' sx={{
        boxShadow: '0 -1px 3px 0 #d8d8d8',
        ...bgBlur({ color: '#FFFFFF' }),
        display: 'flex',
        justifyContent: 'space-evenly',
      }}>
        <IconButton>
          <Link component={RouterLink} href={'/floor'} color="inherit" underline="none" >
            {pathname === '/floor/' ? (
              <CampaignIcon fontSize='large' color='info' />
              ) : (
              <CampaignOutlinedIcon fontSize='large' />
            )}
          </Link>
          {/* 밥순서 */}
        </IconButton>
        <IconButton>
          <Link component={RouterLink} href={'/'} color="inherit" underline="none" >
          <RestaurantMenuOutlinedIcon fontSize='large' color={pathname === '/' ? 'info' : 'action'} />
          </Link>
          {/* 메뉴보기 */}
        </IconButton>
        {isLogin ?(
        <IconButton>
          <Link component={RouterLink} href={'/my-page'} color="inherit" underline="none" >
          {pathname === '/my-page/' ?(
            <AccountCircleIcon fontSize='large' color='info' />
          ) : (
            <AccountCircleOutlinedIcon fontSize='large' />
          )}
          </Link>
          {/* MY */}
        </IconButton>
        ):(
          <IconButton>
          <Link component={RouterLink} href={'/auth/user-login'} color="inherit" underline="none" >
          {pathname === '/auth/user-login/' ?(
            <AccountCircleIcon fontSize='large' color='info' />
          ) : (
            <AccountCircleOutlinedIcon fontSize='large' />
          )}
          </Link>
          {/* MY */}
        </IconButton>
        )}
        <IconButton>
          <Link component={RouterLink} href={'/settings'} color="inherit" underline="none" >
          <SettingsIcon fontSize='large' color={pathname === '/settings/' ? 'info' : 'action'} />
          </Link>
          {/* 설정 */}
        </IconButton>
      </Toolbar>
    </AppBar>
  );
}
