import { useEffect } from 'react';

import Grid from '@mui/material/Unstable_Grid2';
import Container from '@mui/material/Container';

import { useBoolean } from 'src/hooks/use-boolean';

import { SplashScreen } from 'src/components/loading-screen';
import EcommerceProductDetailsCarousel from '../product/details/ecommerce-product-details-carousel';

// ----------------------------------------------------------------------
// product들을 carousel을 감싸는 section

export default function EcommerceProductView() {
  const loading = useBoolean(true);

  useEffect(() => {
    const fakeLoading = async () => {
      await new Promise((resolve) => setTimeout(resolve, 500));
      loading.onFalse();
    };
    fakeLoading();
  }, [loading]);

  if (loading.value) {
    return <SplashScreen />;
  }

  return (
      <Container sx={{ 
        overflow: 'hidden',
        height: `calc(100vh - 172px)`,
      }}>
        <Grid container spacing={{ xs: 5, md: 8 }}
        sx={{
          height:'100%'
        }}>
          <Grid xs={12} md={6} lg={7}
            >
            <EcommerceProductDetailsCarousel />
          </Grid>
        </Grid>
      </Container>
  );
}
