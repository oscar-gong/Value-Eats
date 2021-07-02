import React, { useState, useEffect } from "react";
import NavBar from "../components/Navbar";
import StarRating from "../components/StarRating";
import { MainContainer } from "../styles/MainContainer";
import Carousel from "react-material-ui-carousel";
import { useHistory } from "react-router";
import {
    Card,
    Grid,
    CardContent,
    CardHeader,
    CardMedia,
    Typography,
    makeStyles,
    Select,
    MenuItem,
    InputLabel,
    Box,
} from "@material-ui/core";

export default function EateryProfile({ token }) {
    return (
        <>
            <NavBar token={token} isDiner={true} />
            <MainContainer>
                <Typography variant="h5">Hi guys,</Typography>
            </MainContainer>
        </>
    );
}
