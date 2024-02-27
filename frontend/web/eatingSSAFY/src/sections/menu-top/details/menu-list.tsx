'use client';

import { useRef, useState, useEffect } from 'react';
import { m } from 'framer-motion';

import Paper from '@mui/material/Paper';
import Box from '@mui/material/Box';
import Stack from '@mui/material/Stack';
import Card from '@mui/material/Card';
import Button from '@mui/material/Button';

import Scrollbar from 'src/components/scrollbar';
import { varFade, varContainer, MotionViewport } from 'src/components/animate';

import type { amounts, myMenus, preferences } from '../menu-interface';
import { Divider, Grid, Typography } from '@mui/material';
import React from 'react';
import Speed from './speed';
import ThumbsUpAndDown from './thumbs-up-down';

import { primary } from 'src/theme/palette';
import CurrentInventoryCircular from './current-inventory-circular';

// ----------------------------------------------------------------------

type Props = {
    reqHeight: number;
    amountData: amounts[];
    preferenceData : preferences[];
    listData: myMenus[];
    currentFloor: string;
    currentDate: string;
    isToday: boolean;
}

export default function MenuList({ reqHeight, listData, currentDate, currentFloor, amountData, preferenceData, isToday}: Props) {
    const scrollRef = useRef(null);

    const [allergyView, setAllergyView] = useState<string[]>([]);

    useEffect(() => {
        setAllergyView([]);
    },[currentDate,currentFloor])

    return (
        <>
            <Paper
                key={currentFloor + currentDate}
                ref={scrollRef}
                component={m.div}
                variants={varContainer()}
                sx={{
                    mt:1,
                    height: `${reqHeight}px`,
                    width: 1,
                    backgroundColor: '#00000000'
                }}
            >
                <Scrollbar>
                    {listData.map((item, index) => {
                        const foundPreference = preferenceData.find(o => o.servingAt === item.servingAt && o.category === item.category);

                        return (
                        <Card
                            component={MotionViewport}
                            variants={varFade().inRight}
                            viewport={{ root: scrollRef, once: true, amount: 0.1 }}
                            key={index}
                            sx={{
                                mb: 2,
                                mx: 'auto',
                                border: '1px solid rgba(145, 158, 171, 0.55)',
                                height: '240px',
                                width: 0.97,
                                borderRadius: 1,
                                bgcolor: 'background.paper',
                                boxShadow:'1px 2px 6px 0 rgba(145, 158, 171, 0.4)'
                            }}
                        >
                            <Grid container sx={{height:1, "& > .MuiGrid-root":{height:1}}}>
                                <Grid item xs={1} >
                                    <Typography variant='h6' sx={{borderRight:'1px solid rgba(145, 158, 171, 0.5)',pt:1,backgroundColor:`${primary.main}`, color:'white', opacity:0.8, height:1}} textAlign='center' >
                                        {item.category}
                                    </Typography>
                                </Grid>
                                <Grid item xs={7} sx={{ borderRight: '1.3px dashed rgba(145, 158, 171, 0.5)', display: 'flex', flexDirection: 'column' }}>
                                    {!allergyView.includes(item.category)  || currentFloor==='20층'?
                                        <>
                                            {currentFloor === '10층' ?
                                                <Button
                                                    color='primary'
                                                    size='small'
                                                    sx={{ fontWeight: 'bold', p: 0, borderRadius: 0 }}
                                                    onClick={() => { setAllergyView([...allergyView, item.category]);}}
                                                >
                                                    알러지정보 보기 touch!
                                                </Button>
                                                : undefined}
                                            <Box sx={{ m:'auto',px:0.5, fontSize: '0.8em', whiteSpace: 'pre-wrap', textAlign: 'center', fontWeight:'bold'}}>
                                                {item.foodList.reduce((acc, next)=>{
                                                    acc = acc + next.content;
                                                    if(next.allergyList.length>0) acc += "*";
                                                    return acc+'\n';
                                                }, '')}
                                            </Box>
                                        </> :
                                        <>
                                            <Button
                                                color='primary'
                                                size='small'
                                                sx={{ fontWeight: 'bold', p: 0, borderRadius: 0 }}
                                                onClick={() => {
                                                    const array = allergyView.filter((cont)=> {return cont != item.category;});
                                                setAllergyView(array);}}
                                            >
                                                메뉴확인으로 돌아가기
                                            </Button>
                                            <Stack direction='column' sx={{ m: 'auto', pb: 2 }}>
                                                {item.foodList.map((food, index) => (
                                                    <Box key={food.content} textAlign={'center'} sx={{ px: 1 }}>
                                                    {food.allergyList.length > 0 ?
                                                        <>
                                                            <Box sx={{ ...(food.allergyList.length === 0 && { mb: 0.1 }), fontSize: '0.7rem', fontWeight: 'bold' }}>{food.content}</Box>
                                                            <Box sx={{ fontSize: '0.6rem', fontStyle: 'italic', mb: 0.1 }}>
                                                                {food.allergyList.reduce((acc, next, index) => {
                                                                    acc += next;
                                                                    if (index < food.allergyList.length - 1) acc += ', ';
                                                                    return acc;
                                                                }, '')}
                                                            </Box>
                                                        </>
                                                    : undefined }
                                                    </Box>
                                                ))}
                                            </Stack>
                                        </>}
                                </Grid>
                                <Grid item xs={4} sx={{display:'flex', flexDirection:'column'}}>                              
                                    <Stack direction='column' justifyContent='center' sx={{flexGrow:1, pt:2}}>
                                        {(currentFloor === '10층' && isToday) && <>
                                            <Divider sx={{mb:1, fontSize:'0.85rem', fontWeight:'bold', borderColor:'rgba(145, 158, 171, 0.5)'}}>남은 개수</Divider>
                                            <CurrentInventoryCircular amountData={amountData.find(o => o.category === item.category)}/>
                                            <Speed velocity={amountData.find(o => o.category === item.category)?.velocity || 0}/>
                                            <Typography textAlign='center' variant='caption' sx={{mb:0.5, mt:0.5, px:1}}>{amountData.find(o => o.category === item.category)?.servedAmountPerMin}개/분</Typography>
                                        </>}
                                        <Box sx={{mx:'auto', my:1}}>
                                        {foundPreference && (
                                        <ThumbsUpAndDown isList={true} preferenceData={foundPreference} />
                                        )}
                                        </Box>
                                    </Stack >
                                </Grid>
                            </Grid>
                        </Card>);
                    })}
                    <Box sx={{height:'56px'}}></Box>
                </Scrollbar>
            </Paper>
            
        </>
    );
}

