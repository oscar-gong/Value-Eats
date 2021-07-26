import { shallow } from "enzyme";
import React from "react";
import renderer from "react-test-renderer";
import Loading from "../components/Loading";

describe("Loading", () => {
  it("can work in the base case", () => {
    const loading = shallow(<Loading isLoading={true}/>);
    expect(loading.get(0)).toBeTruthy();
  });

  it("is not shown when asked not to be", () => {
    const loading = shallow(<Loading isLoading={false}/>);
    expect(loading.get(0)).toBeFalsy();
  });

  /* Snapshot tests - creates snapshots for new tests, otherwise will check the existing tests. To update existing tests,
  follow the prompts on the screen after running test. Done to avoid regression. */

  it("renders with minimal props", () => {
    const loading = renderer.create(<Loading isLoading={true}/>);
    expect(loading).toMatchSnapshot();
  });

  it("does not render when asked not to", () => {
    const loading = renderer.create(<Loading isLoading={false}/>);
    expect(loading).toMatchSnapshot();
  });
});
