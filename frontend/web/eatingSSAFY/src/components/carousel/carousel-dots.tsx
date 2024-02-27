/* eslint-disable react/jsx-no-useless-fragment */
import Stack from '@mui/material/Stack';
import Box, { BoxProps } from '@mui/material/Box';
import { Theme, styled, SxProps } from '@mui/material/styles';

// ----------------------------------------------------------------------

type StyledRootProps = {
  rounded: boolean;
};

const StyledRoot = styled(Box, {
  shouldForwardProp: (prop) => prop !== 'rounded',
})<StyledRootProps>(({ rounded, theme }) => ({
  zIndex: 9,
  margin: 0,
  padding: 0,
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  color: 'primary.main',
  '& li': {
    width: 18,
    height: 18,
    opacity: 0.27,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    cursor: 'pointer',
    '&.slick-active': {
      marginLeft: 4,
      marginRight: 4,
      opacity: 0.7,
      ...(rounded && {
        '& span': {
          width: 20,
          borderRadius: 9,
        },
      }),
    },
  },
}));

const StyledDot = styled('span')(({ theme }) => ({
  width: 12,
  height: 8,
  borderRadius: 9,
  // borderRadius: '50%',
  transition: theme.transitions.create(['width'], {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.short,
  }),
}));

// ----------------------------------------------------------------------

export interface Props extends BoxProps {
  rounded?: boolean;
  sx?: SxProps<Theme>;
}

export default function CarouselDots(props?: Props) {
  const rounded = props?.rounded || false;

  const sx = props?.sx;

  return {
    appendDots: (dots: React.ReactNode) => (
      <>
        <StyledRoot component="ul" rounded={rounded} sx={{ ...sx }} {...props}>
          {dots}
        </StyledRoot>
      </>
    ),
    customPaging: () => (
      <Stack
        component="div"
        alignItems="center"
        justifyContent="center"
        sx={{ width: 1, height: 1 }}
      >
        <StyledDot
          sx={{
            bgcolor: 'currentColor',
          }}
        />
      </Stack>
    ),
  };
}
