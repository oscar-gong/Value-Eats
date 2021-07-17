import styled from "styled-components";
import { AppBar } from "@material-ui/core";

export const NavbarStyled = styled(AppBar)`
    background-color: rgba(255, 132, 91, 0.1);
    //background-color: transparent;
    boxshadow: none;
    display: flex;
    color: white;
    justify-content: center;
    align-items: space-between;
    position: static;
    padding: 0px 150px;
    max-height: 10vh;
    min-height: 10vh;
`;
