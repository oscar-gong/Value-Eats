import styled from "styled-components";
import { Button } from "@material-ui/core";

export const ButtonStyled = styled(Button)`
    background: linear-gradient(45deg, #fe6b8b 30%, #ff8e53 90%);
    color: white;
    margin: 4% 0px;
    width: ${(props) => props.widthPercentage}%;
    max-height: 35px;
    &:disabled {
        background: lightgrey;
    }
`;
