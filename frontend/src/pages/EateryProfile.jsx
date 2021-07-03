import React, { } from "react";
import NavBar from "../components/Navbar";
import { MainContainer } from "../styles/MainContainer";
import {
    Typography,
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
