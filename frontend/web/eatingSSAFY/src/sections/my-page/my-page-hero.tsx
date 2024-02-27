'use client';
import Button from '@mui/material/Button';

import { Box, Card, Container, Grid, Stack, Typography, Divider, List, ListItem, ListItemAvatar, ListItemText, ListItemSecondaryAction } from "@mui/material";
import { userPreference } from "./preference-interface";

import { primary } from "src/theme/palette";

import ThumbsUp from "./my-thumbs-up";
import LogoutIcon from '@mui/icons-material/Logout';
import { useRouter } from 'next/navigation';
import Cookies from 'js-cookie';
import useUserStore from 'src/store';
import { useRef, useEffect, useState } from 'react';
import { getUserPreference } from 'src/api/request';
import Scrollbar from 'src/components/scrollbar';

// ----------------------------------------------------------------------

export default function MyPageHero() {

    const loaderRef = useRef<HTMLDivElement>(null);
    const pageIndexRef = useRef<number>(0);
    const [userData, setUserPreference] = useState<userPreference[]>([]);

    const router = useRouter();

    const {
        setIsLogin,
        setUserInfo,
        setPigPower,
    } = useUserStore();

    useEffect(()=> {
        const observer = new IntersectionObserver((entries) => {
            if (entries[0].isIntersecting) {
                getUserPreference({params: {"page":pageIndexRef.current, "pageSize": 10}}).then(response => {
                    if (response.length === 0) {
                        // 데이터가 더 이상 없을 경우 observer를 해제합니다.
                        if (loaderRef.current) {
                            observer.unobserve(loaderRef.current);
                        }
                    } else {
                        setUserPreference(prevData => [...prevData, ...response]);
                        pageIndexRef.current++;
                    }
                  });
            }
        });
    
        if (loaderRef.current) {
            observer.observe(loaderRef.current);
        }
    
        return () => {
            if (loaderRef.current) {
                observer.unobserve(loaderRef.current);
            }
        };
    },[pageIndexRef.current]);

    return (
        <>
            <Container sx={{mb : 4}}>
                <Stack
                    sx={{
                        textAlign: { md: 'center' },
                        height: 1,
                    }}
                >
                    <Card
                        sx={{
                            mt: 2,
                            mb: 2,
                            pb: 1,
                            mx: 'auto',
                            border: '1px solid rgba(145, 158, 171, 0.55)',
                            height: 'auto',
                            width: 0.97,
                            borderRadius: 1,
                            bgcolor: 'background.paper',
                            boxShadow: '1px 2px 6px 0 rgba(145, 158, 171, 0.4)',
                            textAlign: 'center',

                        }}>
                        <Typography
                            variant='h4'
                            sx={{
                                textAlign: 'center',
                                width: 1,
                                py: 0.6,
                                // mb: 2,
                                backgroundColor: `${primary.main}`,
                                color: 'white',
                                opacity: 0.8
                            }}>
                            좋아 😆
                        </Typography>
                        <Scrollbar>
                            <List sx={{ width: '100%', height: '60vh', overflow: 'auto' }}>
                                {userData.map((item) => (
                                    <Box key={item.foodId}>
                                        <ListItem >
                                            <ListItemAvatar>
                                            </ListItemAvatar>
                                            <ListItemText
                                                primary={item.content}
                                                primaryTypographyProps={{ noWrap: true, fontWeight: 'bold', fontSize: 'small' }}
                                            />
                                            <ListItemSecondaryAction>
                                                <ThumbsUp foodId={item.foodId} />
                                            </ListItemSecondaryAction>
                                        </ListItem>
                                        <Divider variant="middle" flexItem />
                                    </Box>
                                ))}
                                <div ref={loaderRef} />
                            </List>
                        </Scrollbar>
                    </Card>
                </Stack>
            </Container>
            <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                <Button sx={{ borderRadius: '16px' }} onClick={() => {Cookies.remove('userId');
                                                                        setIsLogin(false);
                                                                        setUserInfo({"kakaoId":'','personNickname':''});
                                                                        setPigPower(0);
                                                                        router.push('/');}}>
                <LogoutIcon fontSize="small"/>
                <Typography sx={{ fontWeight: 'bold', fontSize: 'small'}}>로그아웃</Typography>
            </Button>
            </Box>
        </>

    );
}