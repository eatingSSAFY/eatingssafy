'use client';

import Link from '@mui/material/Link';
import Button from '@mui/material/Button';
import Divider from '@mui/material/Divider';

import { Box, Card, Container, Grid, Stack } from '@mui/material';

import { logoOrigin } from 'src/components/logo/logoOrigin';
import { backendAPI } from 'src/api/backendAPI';
import { HEADER } from 'src/layouts/config-layout';

// ----------------------------------------------------------------------

export default function UserLoginView() {

  const renderSocials = (
    <Button fullWidth size="large" color="inherit">
      <Link href={`${process.env.NEXT_PUBLIC_API_SERVER_URL ?? 'https://i10a204.p.ssafy.io'}${backendAPI.KAKAO_LOGIN}`}>
        <img src="https://k.kakaocdn.net/14/dn/btroDszwNrM/I6efHub1SN5KCJqLm1Ovx1/o.jpg" width="222" alt="카카오 로그인 버튼" />
      </Link>
    </Button>
  );

  return (
    <Box
      sx={{
        overflow: 'hidden',
        position: 'relative',
        height: {
          md: `calc(100vh - ${HEADER.H_DESKTOP}px)`,
          xs: `calc(100vh - ${HEADER.H_MOBILE}px)`
        },
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        pb: 5,
      }}
    >
      <Box display="flex" flexDirection="column" alignItems="center" gap={3} margin='auto'>
        {logoOrigin}
        <Divider />
        {renderSocials}
      </Box>
    </Box>

  );
}
