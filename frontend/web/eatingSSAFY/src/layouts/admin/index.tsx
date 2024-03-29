import Box, { BoxProps } from '@mui/material/Box';
import Header from './header';
import { HEADER } from '../config-layout';

// ----------------------------------------------------------------------

type Props = BoxProps & {
  children: React.ReactNode;
  headerOnDark?: boolean;
  disabledSpacing?: boolean;
};

export default function AdminLayout({
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
        sx={{
          pt:{ xs: `${HEADER.H_MOBILE}px`, md: `${HEADER.H_DESKTOP}px` },
        }}
      >
        {!(disabledSpacing || headerOnDark) && (
          <Box
            sx={{
              height: { xs: HEADER.H_MOBILE, md: HEADER.H_DESKTOP },
            }}
          />
        )}

        {children}
      </Box>
    </Box>
  );
}
