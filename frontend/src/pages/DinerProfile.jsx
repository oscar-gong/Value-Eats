import React from "react";
import NavBar from "../components/Navbar";
import { MainContent } from "../styles/MainContent";
import { ProfilePhoto } from '../styles/ProfilePhoto'
import { Box, Button, Divider } from "@material-ui/core";
import { StatBox } from "../styles/StatBox";
import EditIcon from '@material-ui/icons/Edit';
import Review from "../components/Review";

export default function DinerProfile({ token }) {
  return (
    <>
      <NavBar token={token} isDiner={false}/>
      <MainContent>
        <Box display="flex" justifyContent="center" alignItems="center" paddingTop="10px">
          <ProfilePhoto size={150} src="https://t4.ftcdn.net/jpg/00/64/67/63/360_F_64676383_LdbmhiNM6Ypzb3FM4PPuFP9rHe7ri8Ju.jpg" />
          <Box display="flex" flexDirection="column" alignItems="center" paddingX="20px">
            <h1>Put ur username here</h1>
            <Button variant="contained"
              color="primary"
              startIcon={<EditIcon />}>
              Edit profile
            </Button>
          </Box>
          <StatBox>
            <h1>5</h1>
            <h1>reviews</h1>
          </StatBox>
          <StatBox>
            <h1>4</h1>
            <h1>photos</h1>
          </StatBox>
        </Box>
        <Box paddingY="30px">
          <Divider variant="middle" />
        </Box>
        {/* Reviews would be mapped here... */}
        <Box display="flex" flexDirection="column" alignItems="center">
          <Review></Review>

        </Box>
      </MainContent>
    </>
  );
}
