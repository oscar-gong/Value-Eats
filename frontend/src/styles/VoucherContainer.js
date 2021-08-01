import styled from "styled-components";
import { Box } from "@material-ui/core";

export const VoucherContainer = styled(Box)`
    width: 100%;
    height: 60vh;
    border: 2px solid #FF845B;
    // background-color: #ffd2bf;
    background-color: white;
    overflow: auto;
    color:  #FF845B;
    @media (max-width: 1250px) {
        & > * {
            margin-left: 5%;
            margin-right: 5%;
        }
    }
`;
