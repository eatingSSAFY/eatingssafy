// import axios from 'axios';
import * as Yup from 'yup';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';

import Stack from '@mui/material/Stack';
import Container from '@mui/material/Container';
import Grid from '@mui/material/Unstable_Grid2';
import Typography from '@mui/material/Typography';
import LoadingButton from '@mui/lab/LoadingButton';

import { useResponsive } from 'src/hooks/use-responsive';

// import Image from 'src/components/image';
import FormProvider, { RHFTextField } from 'src/components/hook-form';

// ----------------------------------------------------------------------

export default function InputForm() {
  const mdUp = useResponsive('up', 'md');

  const TravelContactSchema = Yup.object().shape({
    fullName: Yup.string().required('Full name is required'),
    // email: Yup.string().required('Email is required').email('That is not an email'),
    subject: Yup.string().required('Subject is required'),
    // message: Yup.string().required('Message is required'),
  });

  const defaultValues = {
    fullName: '',
    subject: '',
    // email: '',
    // message: '',
  };

  const methods = useForm({
    resolver: yupResolver(TravelContactSchema),
    defaultValues,
  });

  const {
    reset,
    handleSubmit,
    formState: { isSubmitting },
  } = methods;

  const onSubmit = handleSubmit(async (data) => {
    try {
      // console.log('??')
      // axios를 사용하여 POST 요청을 보냅니다.
      // const response = await axios({
      //   method: 'post',
      //   url: 'http://localhost:5001/no',
      //   data: data,
        // headers: {
        //   'Content-Type': 'application/json',
        //   // 필요하다면 인증 토큰 등 추가
        // },
      // })
      console.log('DATA', data);
      reset();
    } catch (error) {
      console.error(error);
    }
  });

  return (

    <Container
      sx={{
        py: { xs: 10, md: 15 },
      }}
    >
      <Grid container spacing={3} justifyContent="space-between">
        {mdUp && (
          <Grid xs={12} md={6} lg={5}>
            {/* <Image
              alt="travel-contact"
              src="/assets/illustrations/illustration_travel_contact.svg"
            /> */}
          </Grid>
        )}

        <Grid xs={12} md={12} lg={12}>
          <Stack
            spacing={12}
            sx={{
              mb: 5,
              textAlign: { xs: 'center', md: 'left' },
            }}
          >
            <Typography variant="h3">입력폼</Typography>

            <Typography sx={{ color: 'text.secondary' }}>
              Test
            </Typography>
          </Stack>

          <FormProvider methods={methods} onSubmit={onSubmit}>
            <Stack spacing={2.5} alignItems="flex-start">
              <RHFTextField name="fullName" label="이름" />

              {/* <RHFTextField name="email" label="Email" /> */}

              <RHFTextField name="subject" label="학번/사번" />

              {/* <RHFTextField name="message" multiline rows={4} label="Message" sx={{ pb: 2.5 }} /> */}

              <LoadingButton
                size="large"
                type="submit"
                variant="contained"
                color="inherit"
                loading={isSubmitting}
                sx={{
                  alignSelf: { xs: 'center', md: 'unset' },
                }}
              >
                제출
              </LoadingButton>
            </Stack>
          </FormProvider>
        </Grid>
      </Grid>
    </Container>

    // <Container sx={{ py: 10 }}>
    //   <Card sx={{ maxWidth: 480, mx: 'auto', mt: 5 }}>
    //     <CardContent>
    //       <Typography variant="h5" gutterBottom>
    //         입력폼
    //       </Typography>
    //       <Typography sx={{ color: 'text.secondary', mb: 3 }}>
    //         Test
    //       </Typography>

    //       <FormProvider methods={methods} onSubmit={onSubmit}>
    //         <Stack spacing={3}>
    //           <RHFTextField name="fullName" label="이름" />
    //           <RHFTextField name="subject" label="학번/사번" />
    //         </Stack>
    //       </FormProvider>
    //     </CardContent>
    //     <LoadingButton
    //       size="large"
    //       type="submit"
    //       variant="contained"
    //       color="inherit"
    //       loading={isSubmitting}
    //       sx={{ m: 3 }}
    //     >
    //       제출
    //     </LoadingButton>
    //   </Card>
    // </Container>
  );
}
