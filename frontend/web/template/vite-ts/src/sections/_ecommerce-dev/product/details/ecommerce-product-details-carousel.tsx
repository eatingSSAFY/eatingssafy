import { useEffect, useCallback, useState } from 'react';

import Box from '@mui/material/Box';
import Avatar from '@mui/material/Avatar';
import Card from '@mui/material/Card';
import Stack from '@mui/material/Stack';
import Checkbox from '@mui/material/Checkbox';
import Typography from '@mui/material/Typography';
import Link from '@mui/material/Link';
import Divider from '@mui/material/Divider';
import { alpha, styled, useTheme } from '@mui/material/styles';
import Button from '@mui/material/Button';
import Carousel, { useCarousel, CarouselArrows } from 'src/components/carousel';
import Iconify from 'src/components/iconify';
import { bgGradient } from 'src/theme/css';

import { useRecoilValue } from 'recoil';
import { filteredMenuListState } from "../../../../store";
// 파일 위치에 따라서 수정 필요


// import Image from 'src/components/image';
// import Lightbox, { useLightbox } from 'src/components/lightbox';


// ----------------------------------------------------------------------

const THUMB_SIZE = 64;

// const LABEL = [
//   { ID: '도시락', menu: '돼지고기수육(14)\n튀김손만두(6,14)\n알감자조림\n풋고추,쌈장(5)\n부추겉절이(12)\n흑미밥\n근대된장국(5)' },
//   { ID: '샌드위치', menu: '블루베리 크림치즈 베이글\n샌드위치(1,6) & 치킨텐더\n샐러드(1,6,9,15) / 주스' },
//   { ID: '샐러드', menu: '떡갈비 구운마늘\n보울(1,5,14,15,16) / 주스' }
// ];

const StyledThumbnailsContainer = styled('div')<{ length: number }>(({ length, theme }) => ({
  position: 'relative',
  margin: theme.spacing(0, 'auto'),
  '& .slick-slide': {
    lineHeight: 0,
  },

  ...(length === 1 && {
    maxWidth: THUMB_SIZE * 1 + 16,
  }),

  ...(length === 2 && {
    maxWidth: THUMB_SIZE * 2 + 32,
  }),

  ...((length === 3 || length === 4) && {
    maxWidth: THUMB_SIZE * 3 + 48,
  }),

  ...(length >= 5 && {
    maxWidth: THUMB_SIZE * 6,
  }),

  ...(length > 3 && {
    '&:before, &:after': {
      ...bgGradient({
        direction: 'to left',
        startColor: `${alpha(theme.palette.background.default, 0)} 0%`,
        endColor: `${theme.palette.background.default} 100%`,
      }),
      top: 0,
      zIndex: 9,
      content: "''",
      height: '100%',
      position: 'absolute',
      width: (THUMB_SIZE * 2) / 3,
    },
    '&:after': {
      right: 0,
      transform: 'scaleX(-1)',
    },
  }),
}));

// ----------------------------------------------------------------------

// type Props = {
//   images: string[];
// };

export default function EcommerceProductDetailsCarousel() {
  const theme = useTheme();
  const filteredMenuList = useRecoilValue(filteredMenuListState)

  // const slides = images.map((slide) => ({
  //   src: slide,
  // }));

  // const lightbox = useLightbox();

  const carouselLarge = useCarousel({
    rtl: false,
    draggable: false,
    adaptiveHeight: true,
  });

  const carouselThumb = useCarousel({
    rtl: false,
    centerMode: true,
    swipeToSlide: true,
    centerPadding: '5px',
    slidesToShow: filteredMenuList.length === 2 ? 2 : filteredMenuList.length,
    // slidesToShow: slides.length > 3 ? 3 : slides.length,
  });

  useEffect(() => {
    carouselLarge.onSetNav();
    carouselThumb.onSetNav();
  }, [carouselLarge, carouselThumb]);

  // useEffect(() => {
  //   if (lightbox.open) {
  //     carouselLarge.onTogo(lightbox.selected);
  //   }
  // }, [carouselLarge, lightbox.open, lightbox.selected]);

  const [favorite, setFavorite] = useState(true);

  const handleChangeFavorite = useCallback((event: React.ChangeEvent<HTMLInputElement>) => {
    setFavorite(event.target.checked);
  }, []);

  const renderLargeImg = (
    <Box
      sx={{
        mb: 3,
        borderRadius: 2,
        overflow: 'hidden',
        position: 'relative',
        bgcolor: 'background.neutral',
        height: '80%',
      }}
    >
      <Carousel
        {...carouselLarge.carouselSettings}
        asNavFor={carouselThumb.nav}
        ref={carouselLarge.carouselRef}
      >
        {filteredMenuList.length !== 0 ? filteredMenuList.map((menuItem) => (
          <Card key={menuItem.id}>
            <Stack
              direction="row"
              alignItems="center"
              justifyContent="space-between"
              sx={{
                pt: 1.5,
                pl: 2,
                pr: 1.5,
                top: 0,
                width: 1,
                zIndex: 9,
                position: 'absolute',
              }}
            >
              <Checkbox
                color="error"
                checked={favorite}
                onChange={handleChangeFavorite}
                icon={<Iconify icon="carbon:favorite" />}
                checkedIcon={<Iconify icon="carbon:favorite-filled" />}
                sx={{ color: 'common.red' }}
              />
            </Stack>
            <Divider sx={{ borderStyle: 'dashed' }} />

            <Stack spacing={0.5} sx={{ p: 2.5 }}>

              <Typography
                variant="h6" sx={{ whiteSpace: 'pre-wrap', textAlign: 'center' }}
              >
                날짜: {menuItem.날짜}
                메뉴분류명: {menuItem.메뉴분류명} <br />
                메뉴배급장소: {menuItem.메뉴배급장소} <br />
                메뉴상세내용1: {menuItem.메뉴상세내용[0]} <br />
                메뉴상세내용2: {menuItem.메뉴상세내용[1]} <br />
                잔여량: {menuItem.잔여량}
              </Typography>
            </Stack>
          </Card>
        )) : <Card>
          <Stack
            direction="row"
            alignItems="center"
            justifyContent="space-between"
            sx={{
              pt: 1.5,
              pl: 2,
              pr: 1.5,
              top: 0,
              width: 1,
              zIndex: 9,
              position: 'absolute',
            }}
          />
          <Divider sx={{ borderStyle: 'dashed' }} />

          <Stack spacing={0.5} sx={{ p: 2.5 }}>
            <Typography
              variant="h6" sx={{ whiteSpace: 'pre-wrap', textAlign: 'center' }}
            >메뉴가 없습니다.</Typography>
          </Stack>
        </Card>}
      </Carousel>
    </Box>
  );

  const renderThumbnails = (
    <StyledThumbnailsContainer length={filteredMenuList.length}>
      <CarouselArrows onNext={carouselThumb.onNext} onPrev={carouselThumb.onPrev}>
        <Carousel
          {...carouselThumb.carouselSettings}
          asNavFor={carouselLarge.nav}
          ref={carouselThumb.carouselRef}
        >
          {filteredMenuList.map((menuItem, index) => (
            <Box key={menuItem.id} sx={{
              width: 1,
              height: 40,
              opacity: 0.48,
              borderRadius: '2',
              m: 1, // 모든 방향으로 마진 적용
              ...(carouselLarge.currentIndex === index && {
                opacity: 1,
                borderRadius: '2',
              }),
            }}>
              <Button>
                {menuItem.메뉴분류명}
              </Button>
            </Box>
          ))}
        </Carousel>
      </CarouselArrows>
    </StyledThumbnailsContainer>
  );

  return (
      <Box
        sx={{
          '& .slick-slide': {
            float: theme.direction === 'rtl' ? 'right' : 'left',
          },
          // height:'100%'
        }}
      >
        {renderLargeImg}
        {renderThumbnails}
      </Box>
  );
}
