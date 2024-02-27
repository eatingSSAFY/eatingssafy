import Box, { BoxProps } from '@mui/material/Box';
// import Stack from '@mui/material/Stack';

import Header from './header';
import Footer from './footer';

// ----------------------------------------------------------------------

type Props = BoxProps & {
  children: React.ReactNode;
  headerOnDark?: boolean;
  disabledSpacing?: boolean;
};

export default function MainLayout({
  children,
  headerOnDark = false,
  disabledSpacing = false,
  sx,
  ...other
}: Props) {
  return (
    <Box
      sx={{
        height: 1,
        display: 'flex',
        flexDirection: 'column',
        ...sx,
      }}
      {...other}
    >
      <Header headerOnDark={headerOnDark} />
      <Box component="main" 
        // sx={{
        //   pt:{ xs: `${HEADER.H_MOBILE}px`, md: `${HEADER.H_DESKTOP}px` },
        // }}
      >
        {/* {!(disabledSpacing || headerOnDark) && (
          <Box
            sx={{
              height: { xs: HEADER.H_MOBILE, md: HEADER.H_DESKTOP },
            }}
          />
        )} */}

        {children}
      </Box>
      <Footer/>
    </Box>
  );
}
