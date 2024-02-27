import { useEffect, useState } from 'react';

import Box from '@mui/material/Box';
import Card from '@mui/material/Card';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import { Divider } from '@mui/material';
import Stack from '@mui/material/Stack';

import Carousel, { useCarousel, CarouselDots } from 'src/components/carousel';
import CurrentInventoryBar from './current-inventory-bar';
import type { amounts, myMenus, preferences } from '../menu-interface';
import { varFade, MotionViewport } from 'src/components/animate';
import ThumbsUpAndDown from './thumbs-up-down';
import { primary } from 'src/theme/palette';
import Speed from './speed';

// ----------------------------------------------------------------------

type Props = {
  carouselData: myMenus[];
  amountData: amounts[];
  preferenceData : preferences[];
  currentFloor: string;
  currentDate: string;
  isToday: boolean;
}

export default function MenuCarousel({ carouselData, currentFloor, currentDate, amountData, preferenceData, isToday }: Props) {
  // ----------------------------------------------------------------------
  const [allergyView, setAllergyView] = useState<string[]>([]);

  const carouselLarge = useCarousel({
    rtl: false,
    draggable: true,
    infinite: false,
    // lazyLoad: 'progressive',
    swipeToSlide:true,
    ...CarouselDots({
      rounded:true,
    })
  });

  useEffect(() => {
    carouselLarge.onSetNav();
  }, [carouselLarge]);

  const renderLargeImg = (
    <Carousel
      {...carouselLarge.carouselSettings}
      ref={carouselLarge.carouselRef}
    >
      {carouselData.map((item, index) => {
        const foundPreference = preferenceData.find(o => o.servingAt === item.servingAt && o.category === item.category);

        return (
        <Box key={"box_" + index} sx={{
          height:1,
          px:1.5,
          pt:1
        }}>
          <Card
            key={"carousel_" + index}
            variant='outlined'
            sx={{
              borderColor: 'rgba(145, 158, 171, 0.55)',
              // flexGrow: 1,
              height: '97%',
              "& > .MuiDivider-root": { border: '1px dashed rgba(145, 158, 171, 0.3)' },
              boxShadow:'1px 2px 6px 0 rgba(145, 158, 171, 0.4)'
            }}
          >
            <Box
              sx={{
                height: 1,
                maxHeight:1,
                display: 'flex',
                flexDirection: 'column',
              }}
            >
              <Typography 
                variant='h4' 
                sx={{
                  textAlign:'center', 
                  width:1, 
                  py:0.6, 
                  backgroundColor:`${primary.main}`, 
                  color:'white', 
                  opacity:0.8
              }}>
                {item.category}
              </Typography>
              <Divider sx={{borderColor:'rgba(145, 158, 171, 0.5)'}}/>
              { !allergyView.includes(item.category) || currentFloor === '20층' ? 
                <>
                  {currentFloor === '10층' ?
                  <Button
                    color='primary'
                    size='small'
                    sx={{ fontWeight: 'bold', p: 0, borderRadius: 0 }}
                    onClick={() => {setAllergyView([...allergyView, item.category]);}}
                  >
                    알러지정보 보기 touch!
                  </Button>
                  : undefined}
                  <Box whiteSpace={'pre-wrap'} textAlign='center' sx={{ m: 'auto', fontWeight: 'bold', fontSize: '0.9rem' }}>
                    {item.foodList.reduce((acc, next) => {
                      acc = acc + next.content;
                      if (next.allergyList.length > 0) acc += "*";
                      return acc + '\n';
                    }, '')}
                  </Box>
                  
                  <Divider sx={{
                    "&::before": { borderColor: 'rgba(145, 158, 171, 0.5)' },
                    "&::after": { borderColor: 'rgba(145, 158, 171, 0.5)' },
                    ...((currentFloor === '20층' || !isToday) && {
                      mb: 3
                    })
                  }}>
                    {foundPreference && (
                    <ThumbsUpAndDown isList={false} preferenceData={foundPreference} />
                    )}
                  </Divider>
                  {currentFloor === '10층' && isToday ?
                    <>
                      <CurrentInventoryBar amountData={amountData.find(o => o.category === item.category)} isCarousel={true} />
                      <Stack direction='row' justifyContent='center'>
                        <Speed velocity={amountData.find(o => o.category === item.category)?.velocity || 0}/>
                        <Speed velocity={amountData.find(o => o.category === item.category)?.velocity || 0}/>
                      </Stack>
                      <Typography textAlign='center' variant='caption' sx={{ mb: 1 }}>1분당 {amountData.find(o => o.category === item.category)?.servedAmountPerMin}개가 나가고 있어요!</Typography>
                    </> : undefined}
                </> : 
                <>
                  <Button
                    color='primary'
                    size='small'
                    sx={{ fontWeight: 'bold', p: 0, borderRadius: 0 }}
                    onClick={() => {
                      const array = allergyView.filter((cont)=> {return cont != item.category;})
                      setAllergyView(array);
                    }}
                  >
                    메뉴확인으로 돌아가기
                  </Button>
                  <Stack direction='column' sx={{ m:'auto', pb:2}}>
                    {item.foodList.map((food, index) => (
                      <Box key={food.content} textAlign={'center'} sx={{px:1}}>
                        <Box sx={{...(food.allergyList.length === 0 && {mb:0.7}),fontSize:'0.8rem', fontWeight:'bold'}}>{food.content}</Box>
                        {food.allergyList.length > 0 ? 
                          <Box sx={{ fontSize:'0.75rem',fontStyle:'italic' ,mb:0.7}}>
                            {food.allergyList.reduce((acc, next, index)=>{
                              acc += next;
                              if(index < food.allergyList.length -1) acc += ', ';
                              return acc;
                            },'')}
                          </Box>
                        : undefined}
                      </Box>
                    ))}
                  </Stack>
              </>
              }
            </Box>
          </Card>
        </Box>);
      })}
    </Carousel>
  );

  useEffect(() => {
    carouselLarge.setCurrentIndex(0);
    setAllergyView([]);
  }, [currentDate, currentFloor]);

  
  return (
    < >
      <Box
        key={currentDate + currentFloor}
        sx={{display: 'flex', flexDirection: 'column', height: 1, }}
      >
        <Box
          sx={{
            flexGrow: 1,
            '& .slick-list, & .slick-slide > div, & .slick-track': {
              height: '100%'
            },
            '& .slick-slider': {
              height: '83%'
            },
          }}
          component={MotionViewport}
          variants={varFade().in}
        >
          {renderLargeImg}
        </Box>
      </Box>
    </>
  );
}
