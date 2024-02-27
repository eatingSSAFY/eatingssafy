import { Box, Card, Container, Grid, Stack, Typography, Paper, Chip } from "@mui/material";

import { HEADER } from 'src/layouts/config-layout';
import { LunchTimeData } from "./type";
import { primary } from "src/theme/palette";

import { useEffect, useState } from "react";

import { styled } from "@mui/material/styles";
import { thisWeekDates, monthIsOver10, dateIsOver10 } from "../menu-top/menu-constants";

// ----------------------------------------------------------------------
const Item = styled(Paper)(({ theme }) => ({
  ...theme.typography.body2,
  textAlign: "center",
  height: 60,
  lineHeight: "30px",
}));

function checkIfLunchTime(lunchTime: string) {
  const now = new Date();
  const start = lunchTime.split('~').map(timeStr => {
    const [hour, minute] = timeStr.split(':').map(Number);
    const date = new Date();
    date.setHours(hour);
    date.setMinutes(minute);
    return date;
  })[0];

  return now >= start;
}

export default function FloorHero({ floors }: { floors: LunchTimeData }) {

  const startDate: string = thisWeekDates[0]
  const endDate: string = thisWeekDates[4]

  return (
    <Box
      sx={{
        overflow: 'hidden',
        position: 'relative',
        height: {
          md: `calc(100vh - ${HEADER.H_DESKTOP}px)`,
          xs: `calc(100vh - ${HEADER.H_MOBILE}px)`
        },
      }}
    >
      <Container sx={{ height: 1 }}>
        <Stack
          sx={{
            textAlign: { md: 'center' },
            height: 1,
          }}
        >
          <Grid container>
            <Grid item xs={12}></Grid>
            <>
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
                  textAlign: 'center'
                }}>
                <Typography
                  variant='h4'
                  sx={{
                    textAlign: 'center',
                    width: 1,
                    py: 0.6,
                    mb: 2,
                    backgroundColor: `${primary.main}`,
                    color: 'white',
                    opacity: 0.8
                  }}>
                  🍚 밥 시간 🍚
                </Typography>
                <Grid container spacing={2}>
                  <Grid item xs={12}>
                    <Box
                      sx={{
                        p: 2,
                        borderRadius: 2,
                        bgcolor: "background.default",
                        display: "grid",
                        gridTemplateColumns: "1fr 1fr",
                        gap: 2,
                      }}
                    >
                      {floors.map((item, index) => {
                        // 그림자 조절
                        const isLunchTime: boolean = checkIfLunchTime(item.lunchTime)
                        return (
                          <Item
                            key={`floor_${item.floor}`}
                            elevation={6}
                            sx={{ backgroundColor: isLunchTime ? '#46D2FF' : undefined }}>
                            <Typography
                              variant="button"
                              fontWeight="bold"
                              gutterBottom
                              color={isLunchTime ? '#000080' : undefined}>{`${item.floor}층`} {isLunchTime ? '🍴' : undefined}</Typography>
                            <br />
                            <Typography
                              variant="button"
                              gutterBottom
                              color={isLunchTime ? '#000080' : undefined}>{item.lunchTime}</Typography>
                          </Item>
                        )
                      })}
                    </Box>
                  </Grid>
                </Grid>
                <Typography variant="h3" gutterBottom>{startDate.slice(6 - monthIsOver10, 7)}/{startDate.slice(9 - dateIsOver10, 10)} ~ {endDate.slice(6 - monthIsOver10, 7)}/{endDate.slice(9 - dateIsOver10, 10)}</Typography>
              </Card>
            </>
          </Grid>
        </Stack>
      </Container>
    </Box>
  );
}