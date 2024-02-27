import { useState } from 'react';

import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import { ThumbUpOffAlt, ThumbUpAlt, ThumbDownOffAlt, ThumbDownAlt } from '@mui/icons-material';
import { addUserPreference } from 'src/api/request';
import { preferences } from '../menu-interface';
import useUserStore from 'src/store';
import { useRouter } from "next/navigation";

type Props = {
    preferenceData: preferences,
    isList : boolean,
}

export default function ThumbsUpAndDown({isList, preferenceData} : Props) {
    // null, false, true
    const [isLiked, setIsLiked] = useState(preferenceData.like === null ? false : preferenceData.like);
    const [isDisLiked, setIsDisLiked] = useState(preferenceData.like === null ? false : !preferenceData.like);

    const [likeCnt, setLikeCnt] = useState(preferenceData.likeCnt);
    const [dislikeCnt, setDislikeCnt] = useState(preferenceData.dislikeCnt);

    const router = useRouter();

    const {
      isLogin,
    } = useUserStore();

    return (
        <Box sx={{my:0.5}}>
            <IconButton
                sx={{p:0.3}}
                aria-label="Like"
                onClick={() => {
                    if (!isLogin) {
                      router.push('/auth/user-login');
                      return;
                    } 
                    setIsLiked(!isLiked);
                    // 싫어요 상태일 때
                    if (isDisLiked) {
                      setIsDisLiked(!isDisLiked);
                      setDislikeCnt(dislikeCnt-1);
                      setLikeCnt(likeCnt+1);
                      addUserPreference({"foodId":preferenceData.foodId, "value":1});
                    }
                    // 좋아요 상태일 때
                    else if (isLiked) {
                      setLikeCnt(likeCnt-1);
                      addUserPreference({"foodId": preferenceData.foodId, "value":0});
                    }
                    // 아무것도 아닐 때
                    else {
                      setLikeCnt(likeCnt+1);
                      addUserPreference({"foodId":preferenceData.foodId, "value":1});
                    }
                  }}>
                    {isLiked ? <ThumbUpAlt color="info"/> : <ThumbUpOffAlt />}
            </IconButton>
            <Button disabled size='small' sx={{minWidth:0, px:0, fontSize:'1em', "&.Mui-disabled":{color:'#00000090', mr:1}}}>{likeCnt}</Button>
            {isList ? <br/> : undefined}
            <IconButton
                sx={{p:0.3}}
                aria-label="DisLike"
                onClick={() => {
                                if (!isLogin) {
                                  router.push('/auth/user-login');
                                  return;
                                } 
                                setIsDisLiked(!isDisLiked);
                                // 좋아요 상태일 때
                                 if (isLiked) {
                                     setIsLiked(!isLiked)
                                     setLikeCnt(likeCnt-1)
                                     setDislikeCnt(dislikeCnt+1)
                                     addUserPreference({"foodId":preferenceData.foodId, "value":-1})
                                }
                                // 싫어요 상태일 때
                                 else if (isDisLiked) {
                                     setDislikeCnt(dislikeCnt-1)
                                     addUserPreference({"foodId": preferenceData.foodId, "value":0});
                                // 아무것도 아닐 때
                                 } else {
                                     setDislikeCnt(dislikeCnt+1)
                                     addUserPreference({"foodId":preferenceData.foodId, "value":-1})
                                 } }}>
                    {isDisLiked ? <ThumbDownAlt color='error'/> : <ThumbDownOffAlt />}
            </IconButton>
            <Button disabled size='small' sx={{minWidth:0, px:0, fontSize:'1em', "&.Mui-disabled":{color:'#00000090'}}}>{dislikeCnt}</Button>
        </Box>
    )
}