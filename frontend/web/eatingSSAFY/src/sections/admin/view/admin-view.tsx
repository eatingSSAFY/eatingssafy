import { Grid, Stack, Typography } from '@mui/material';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import MenuView from 'src/sections/menu-top/view/menu-view';
import MenuList from 'src/sections/menu-top/details/menu-list';
import Button from '@mui/material/Button';
import MenuHeader from 'src/sections/menu-top/menu-header';

// ----------------------------------------------------------------------

export default function AdminPageView() {
    return (
      <Box
      sx={{
        py: 20,
        position: 'relative',
        // ...bgGradient({
        //   startColor: `${alpha(theme.palette.common.black, 0)} 0%`,
        //   endColor: `${theme.palette.common.black} 75%`,
        //   imgUrl: post.heroUrl,
        // }),
      }}
    >
      <Container>
        {/* 왼쪽 */}
        <Grid container spacing={3}>
          <Grid item xs={12} md={6} border={1}>
            <Stack
              spacing={3}
              alignItems={{
                xs: 'center',
                md: 'flex-start',
              }}
              sx={{
                color: 'common.black',
                textAlign: {
                  xs: 'center',
                  md: 'left',
                },
              }}
            >
              <Typography variant="h3" component="h1">
                대시보드
              </Typography>

            </Stack>
          </Grid>
          {/* 오른쪽 */}
          <Grid item xs={12} md={6} border={1}>
            <Stack
              spacing={3}
              alignItems={{
                xs: 'center',
                md: 'flex-start',
              }}
              sx={{
                color: 'common.black',
                textAlign: {
                  xs: 'center',
                  md: 'left',
                },
              }}
            >
              <Typography variant="h3" component="h1">
                메뉴 리스트
              </Typography>
              <Typography>10층</Typography>
              
              <Button>메뉴 수정하기</Button>
            </Stack>
          </Grid>
        </Grid>
      </Container>
    </Box>
      );
}
